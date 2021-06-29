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
package java_cup;

/** This class contains version and authorship information. 
 *  It contains only static data elements and basically just a central 
 *  place to put this kind of information so it can be updated easily
 *  for each release.  
 *
 *  Version numbers used here are broken into 3 parts: major, minor, and 
 *  update, and are written as v[major].[minor].[update] (e.g. v0.10a).
 *  Major numbers will change at the time of major reworking of some 
 *  part of the system.  Minor numbers for each public release or 
 *  change big enough to cause incompatibilities.  Finally update
 *  letter will be incremented for small bug fixes and changes that
 *  probably wouldn't be noticed by a user.  
 *
 * @version last updated: 12/22/97 [CSA]
 * @author  Frank Flannery
 */

public class version {
  /** The major version number. */
  public static final int major = 0;

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** The minor version number. */
  public static final int minor = 10;

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** The update letter. */
  public static final char update = 'k';

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** String for the current version. */
  public static final String version_str = "v" + major + "." + minor + update;

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** Full title of the system */
  public static final String title_str = "CUP " + version_str;

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** Name of the author */
  public static final String author_str =
      "Scott E. Hudson, Frank Flannery, and C. Scott Ananian";

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** The command name normally used to invoke this program */ 
  public static final String program_name = "java_cup";
}