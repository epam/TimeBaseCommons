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

/** This class represents a shift/reduce nonassociative error within the 
 *  parse table.  If action_table element is assign to type
 *  nonassoc_action, it cannot be changed, and signifies that there 
 *  is a conflict between shifting and reducing a production and a
 *  terminal that shouldn't be next to each other.
 *
 * @version last updated: 7/2/96
 * @author  Frank Flannery
 */
public class nonassoc_action extends parse_action {
 
  /*-----------------------------------------------------------*/
  /*--- Constructor(s) ----------------------------------------*/
  /*-----------------------------------------------------------*/

  /** Simple constructor. 
   */
  public nonassoc_action() throws internal_error
    {
	/* don't need to set anything, since it signifies error */
    }

    /*-----------------------------------------------------------*/
  /*--- General Methods ---------------------------------------*/
  /*-----------------------------------------------------------*/

  /** Quick access to type of action. */
  public int kind() {return NONASSOC;}

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** Equality test. */
  public boolean equals(parse_action other)
    {
      return other != null && other.kind() == NONASSOC;
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
      return 0xCafe321;
    }



  /** Convert to string. */
  public String toString() 
    {
      return "NONASSOC";
    }

  /*-----------------------------------------------------------*/

}