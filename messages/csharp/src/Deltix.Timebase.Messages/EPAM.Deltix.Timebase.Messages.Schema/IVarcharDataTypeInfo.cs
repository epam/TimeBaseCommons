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
using System;


namespace EPAM.Deltix.Timebase.Messages.Schema
{
    
    
    /// <summary>
    /// Schema definition of varchar data type.
    /// </summary>
    public interface IVarcharDataTypeInfo : IDataTypeInfo
    {
        
        #region Properties
        /// <summary>
        /// True, if field allows line breaks.
        /// </summary>
        Boolean IsMultiline
        {
            get;
        }
        
        /// <summary>
        /// Encoding type.
        /// </summary>
        UInt32 EncodingType
        {
            get;
        }
        
        /// <summary>
        /// Length.
        /// </summary>
        UInt32 Length
        {
            get;
        }
        #endregion
        
        #region Methods
        /// <summary>
        /// Flag that indicates whether property IsMultiline has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        Boolean HasIsMultiline();
        
        /// <summary>
        /// Flag that indicates whether property EncodingType has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        Boolean HasEncodingType();
        
        /// <summary>
        /// Flag that indicates whether property Length has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        Boolean HasLength();
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        new IVarcharDataTypeInfo Clone();
        #endregion
    }
}