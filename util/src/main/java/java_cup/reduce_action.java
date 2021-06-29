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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/** This class represents a reduce action within the parse table.
 *  The action simply stores the production that it reduces with and 
 *  responds to queries about its type.
 *
 * @version last updated: 11/25/95
 * @author  Scott Hudson
 */
public class reduce_action extends parse_action {
 
  /*-----------------------------------------------------------*/
  /*--- Constructor(s) ----------------------------------------*/
  /*-----------------------------------------------------------*/

  /** Simple constructor. 
   * @param prod the production this action reduces with.
   */
  public reduce_action(production prod ) throws internal_error
    {
      /* sanity check */
      if (prod == null)
	throw new internal_error(
	  "Attempt to create a reduce_action with a null production");

      _reduce_with = prod;
    }

  /*-----------------------------------------------------------*/
  /*--- (Access to) Instance Variables ------------------------*/
  /*-----------------------------------------------------------*/
  
  /** The production we reduce with. */
  protected production _reduce_with;

  /** The production we reduce with. */
  public production reduce_with() {return _reduce_with;}

  /*-----------------------------------------------------------*/
  /*--- General Methods ---------------------------------------*/
  /*-----------------------------------------------------------*/

  /** Quick access to type of action. */
  public int kind() {return REDUCE;}

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** Equality test. */
  public boolean equals(reduce_action other)
    {
      return other != null && other.reduce_with() == reduce_with();
    }

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** Generic equality test. */
  @SuppressFBWarnings(value = "EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC", justification = "Not touching Java_cup - third-party source")
  public boolean equals(Object other)
    {
      if (other instanceof reduce_action)
	return equals((reduce_action)other);
      else
       return false;
    }

  /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

  /** Compute a hash code. */
  public int hashCode()
    {
      /* use the hash code of the production we are reducing with */
      return reduce_with().hashCode();
    }


  /** Convert to string. */
  public String toString() 
    {
      return "REDUCE(with prod " + reduce_with().index() + ")";
    }

  /*-----------------------------------------------------------*/

}