/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.os;

import java.io.*;
import java.util.*;

public class DotNetFrameworkVersionTester {

	private static final String REGQUERY_UTIL = "reg query ";
	private static final String DOT_NET_CMD   = REGQUERY_UTIL + "HKLM\\SOFTWARE\\Microsoft\\.NETFramework\\policy";

	public static boolean compareVersion ( String existing,
	                                       String required ) {
		return (existing.compareTo ( required ) >= 0);
	}

	public static boolean checkDotNetVersionRegistryKeyExistance ( String version ) {
		String[] versions = getDotNetVersions ( );
		if (versions != null) {
			for (String vers : versions) {
				if (compareVersion ( vers,
				                     version ))
					return true;
			}
		}
		return false;
	}

	public static String[] getDotNetVersions ( ) {
		try {
			ArrayList<String> resultArray = new ArrayList<String> ( );
			Process process = Runtime.getRuntime ( ).exec ( DOT_NET_CMD );
			StreamReader reader = new StreamReader ( process.getInputStream ( ) );

			reader.start ( );
			process.waitFor ( );
			reader.join ( );

			String result = reader.getResult ( );
			String keys[] = result.split ( "\r\n" );
			for (String key : keys) {
				if (!key.isEmpty ( )) {
					int policyIndex = key.indexOf ( "\\policy" );
					if (policyIndex > 0) {
						if (key.length ( ) > policyIndex + 8) {
							String subKey = key.substring ( policyIndex + 8 );
							if (subKey.startsWith ( "v" )) {

								String version = subKey.substring ( 1 );
								int pointIndex = version.indexOf ( '.' );
								if (pointIndex > 0) {
									String major = version.substring ( 0,
									                                   pointIndex );
									String minor = version.substring ( pointIndex + 1 );
									try {
										Short.parseShort ( major );
										Short.parseShort ( minor );
										resultArray.add ( version );
									} catch (NumberFormatException nfe) {
										continue;
									}
								}
							}
						}
					}
				}
			}
			return resultArray.toArray ( new String[resultArray.size ( )] );
		} catch (Exception e) {
            throw new RuntimeException(e);
			//return null;
		}
	}

	static class StreamReader extends Thread {
		private InputStream  is;
		private StringWriter sw;

		StreamReader ( InputStream is ) {
			this.is = is;
			sw = new StringWriter ( );
		}

		public void run ( ) {
			try {
				int c;
				while ((c = is.read ( )) != -1)
					sw.write ( c );
			} catch (IOException e) {
				;
			}
		}

		String getResult ( ) {
			return sw.toString ( );
		}
	}

	public static void main ( String s[] ) {
		System.out.println ( "DotNET v2.0 is installed: " + checkDotNetVersionRegistryKeyExistance ( "2.0" ) );
		System.out.println ( "Installed DotNET versions : " );
		String[] versions = getDotNetVersions ( );
		for (String version : versions)
			System.out.println ( version );
	}
}