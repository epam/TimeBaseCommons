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

/** This class serves as the base class for entries in a parse action table.  
 *  Full entries will either be SHIFT(state_num), REDUCE(production), NONASSOC,
 *  or ERROR. Objects of this base class will default to ERROR, while
 *  the other three types will be represented by subclasses. 
 * 
 * @see     java_cup.reduce_action
 * @see     java_cup.shift_action
 * @version last updated: 7/2/96
 * @author  Frank Flannery
 */

public class parse_action {

  /*-----------------------------------------------------------*/
  /*--- Constructor(s) ----------------------------------------*/
  /*-----------------------------------------------------------*/

  /** Simple constructor. */
  public parse_action()
    {
      /* nothing to do in the base class */
    }

 
  /*-----------------------------------------------------------*/
  /*--- (Access to) Static (Class) Variables ------------------*/
  /*-----------------------------------------------------------*/

  /** Constant for action type -- error action. */
  public static final int ERROR = 0;

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** Constant for action type -- shift action. */
  public static final int SHIFT = 1;

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** Constants for action type -- reduce action. */
  public static final int REDUCE = 2;

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** Constants for action type -- reduce action. */
  public static final int NONASSOC = 3;

  /*-----------------------------------------------------------*/
  /*--- General Methods ---------------------------------------*/
  /*-----------------------------------------------------------*/
	 
  /** Quick access to the type -- base class defaults to error. */
  public int kind() {return ERROR;}

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** Equality test. */
  public boolean equals(parse_action other)
    {
      /* we match all error actions */
      return other != null && other.kind() == ERROR;
    }

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** Generic equality test. */
  public boolean equals(Object other)
    {
      if (other instanceof parse_action)
	return equals((parse_action)other);
      else
	return false;
    }
  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** Compute a hash code. */
  public int hashCode()
    {
      /* all objects of this class hash together */
      return 0xCafe123;
    }

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** Convert to string. */
  public String toString() {return "ERROR";}

  /*-----------------------------------------------------------*/
}