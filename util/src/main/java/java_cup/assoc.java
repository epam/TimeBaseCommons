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

/* Defines integers that represent the associativity of terminals
 * @version last updated: 7/3/96
 * @author  Frank Flannery
 */

public class assoc {

  /* various associativities, no_prec being the default value */
  public final static int left = 0;
  public final static int right = 1;
  public final static int nonassoc = 2;
  public final static int no_prec = -1;

}