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
    /// Type of transformation applied to data field.
    /// </summary>
    [SchemaElement(Name="com.epam.deltix.timebase.messages.schema.SchemaFieldDataTransformationType", Title="SchemaFieldDataTransformationType", Description=null)]
    public enum SchemaFieldDataTransformationType
    {
        
        /// <summary>
        /// Convert data operation.
        /// </summary>
        [SchemaElement(Name="CONVERT_DATA", Title=null, Description=null)]
        ConvertData = 0,
        
        /// <summary>
        /// Apply default value operation.
        /// </summary>
        [SchemaElement(Name="SET_DEFAULT", Title=null, Description=null)]
        SetDefault = 1,
        
        /// <summary>
        /// Drop record operation.
        /// </summary>
        [SchemaElement(Name="DROP_RECORD", Title=null, Description=null)]
        DropRecord = 2,
    }
}