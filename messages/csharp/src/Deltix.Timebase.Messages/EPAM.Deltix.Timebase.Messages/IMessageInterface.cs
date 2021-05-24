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


namespace EPAM.Deltix.Timebase.Messages
{
    
    
    /// <summary>
    /// Base class for all messages that could be written in Timebase.
    /// </summary>
    public interface IMessageInterface : IMessageInfo, IRecordInterface
    {
        
        #region Properties
        /// <summary>
        /// 
        /// Gets message time in milliseconds that passed since January 1, 1970, 00:00:00 GMT
        /// 
        /// </summary>
        new DateTime TimeStampMs
        {
            get;
            set;
        }
        
        /// <summary>
        /// 
        /// Gets message time in nanoseconds that passed since January 1, 1970, 00:00:00 GMT
        /// 
        /// </summary>
        new HdDateTime NanoTime
        {
            get;
            set;
        }
        
        /// <summary>
        /// Instrument name.
        /// </summary>
        new String Symbol
        {
            get;
            set;
        }
        #endregion
        
        #region Methods
        /// <summary>
        /// Sets null to TimeStampMs property.
        /// </summary>
        void NullifyTimeStampMs();
        
        /// <summary>
        /// Sets null to NanoTime property.
        /// </summary>
        void NullifyNanoTime();
        
        /// <summary>
        /// Sets null to Symbol property.
        /// </summary>
        void NullifySymbol();
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        new IMessageInterface Clone();
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        new IMessageInterface CopyFrom(IRecordInfo source);
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        new IMessageInterface Nullify();
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        new IMessageInterface Reset();
        #endregion
    }
}