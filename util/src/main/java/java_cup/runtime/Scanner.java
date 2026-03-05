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
package java_cup.runtime;

/**
 * Defines the Scanner interface, which CUP uses in the default
 * implementation of <code>lr_parser.scan()</code>.  Integration
 * of scanners implementing <code>Scanner</code> is facilitated.
 *
 * @version last updated 23-Jul-1999
 * @author David MacMahon davidm@smartsc.com
 */

/* *************************************************
  Interface Scanner
  
  Declares the next_token() method that should be
  implemented by scanners.  This method is typically
  called by lr_parser.scan().  End-of-file can be
  indicated either by returning
  <code>new Symbol(lr_parser.EOF_sym())</code> or
  <code>null</code>.
 ***************************************************/
public interface Scanner {
    /** Return the next token, or <code>null</code> on end-of-file. */
    public Symbol next_token() throws java.lang.Exception;
}