#tool nuget:?package=NUnit.ConsoleRunner&version=3.7.0

//////////////////////////////////////////////////////////////////////
// ARGUMENTS
//////////////////////////////////////////////////////////////////////

var target = Argument("target", "Default");
var configuration = Argument("configuration", "Release");

//////////////////////////////////////////////////////////////////////
// PREPARATION
//////////////////////////////////////////////////////////////////////

var gradleProperties = new Dictionary<String, String>();
foreach (var row in System.IO.File.ReadAllLines("../../gradle.properties"))
    gradleProperties.Add(row.Split('=')[0], String.Join("=",row.Split('=').Skip(1).ToArray()));
var version = gradleProperties["version"];
var index = version.IndexOf("-");
var dotNetVersion = (index > 0 ? version.Substring(0, index) : version) + ".0";

//////////////////////////////////////////////////////////////////////
// TASKS
//////////////////////////////////////////////////////////////////////

Task("Clean")
    .Does(() =>
{
    DotNetCoreClean("./Deltix.Timebase.Messages.sln");
});

Task("Restore-NuGet-Packages")
    .IsDependentOn("Clean")
    .Does(() =>
{
    DotNetCoreRestore("./Deltix.Timebase.Messages.sln");
});

Task("Build")
    .IsDependentOn("Restore-NuGet-Packages")
    .Does(() =>
{
    DotNetCoreBuild("./Deltix.Timebase.Messages.sln", new DotNetCoreBuildSettings {
        Configuration = configuration,
        MSBuildSettings = new DotNetCoreMSBuildSettings()
            .WithProperty("Version", version)
            .WithProperty("FileVersion", dotNetVersion)
            .WithProperty("AssemblyVersion", dotNetVersion)
    });
});

Task("Pack")
    .IsDependentOn("Build")
    .Does(() =>
{
    var settings = new DotNetCorePackSettings
    {
        Configuration = configuration,
        OutputDirectory = "./artifacts/",
        MSBuildSettings = new DotNetCoreMSBuildSettings()
            .WithProperty("IncludeSymbols", "true")
            .WithProperty("IncludeSource", "true")
            .WithProperty("Version", version)
            .WithProperty("FileVersion", dotNetVersion)
            .WithProperty("AssemblyVersion", dotNetVersion)
    };
    DotNetCorePack(".", settings);
});

Task("Push")
    .IsDependentOn("Pack")
    .Does(() =>
{
    var url = "https://artifactory.epam.com/artifactory/api/nuget/EPM-RTC-net/";
	var usr = EnvironmentVariable("ARTIFACTORY_USER");
	var psw = EnvironmentVariable("ARTIFACTORY_PASS");
	
	if (usr == "" || usr == null)
		throw new Exception("ARTIFACTORY_USER is not defined");
	
	if (psw == "" || psw == null)
		throw new Exception("ARTIFACTORY_PASS is not defined");
		
    var apiKey = usr + ":" + psw;
	    
    foreach (var file in GetFiles("./artifacts/*.symbols.nupkg"))
    {
        var group = file.GetFilename().ToString().Replace(".symbols.nupkg", "").Replace("." + version, "");
        DotNetCoreTool(".", "nuget", "push " + file.FullPath + " --source " + url + group + " --api-key " + apiKey);
    }
});

//////////////////////////////////////////////////////////////////////
// TASK TARGETS
//////////////////////////////////////////////////////////////////////

Task("Default")
    .IsDependentOn("Build");

//////////////////////////////////////////////////////////////////////
// EXECUTION
//////////////////////////////////////////////////////////////////////

RunTarget(target);
