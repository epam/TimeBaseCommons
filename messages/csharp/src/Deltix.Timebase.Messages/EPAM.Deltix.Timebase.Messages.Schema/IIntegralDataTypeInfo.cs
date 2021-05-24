/*
  Copyright 2021 EPAM Systems, Inc

  See the NOTICE file distributed with this work for additional information
  regarding copyright ownership. Licensed under the Apache License,
  Version 2.0 (the "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
  License for the specific language governing permissions and limitations under
  the License.
 */
//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.42000
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

using EPAM.Deltix.Timebase.Messages;
using com.epam.deltix.containers.;
using System;


namespace EPAM.Deltix.Timebase.Messages.Schema
{
    
    
    /// <summary>
    /// This is a base class for numeric data types.
    /// </summary>
    public interface IIntegralDataTypeInfo : IDataTypeInfo
    {
        
        #region Properties
        /// <summary>
        /// String definition for minimum constraint.
        /// </summary>
        MutableString MinValue
        {
            get;
        }
        
        /// <summary>
        /// String definition for maximum constraint.
        /// </summary>
        MutableString MaxValue
        {
            get;
        }
        #endregion
        
        #region Methods
        /// <summary>
        /// Flag that indicates whether property MinValue has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        Boolean HasMinValue();
        
        /// <summary>
        /// Flag that indicates whether property MaxValue has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        Boolean HasMaxValue();
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        new IIntegralDataTypeInfo Clone();
        #endregion
    }
}