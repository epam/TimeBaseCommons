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


namespace EPAM.Deltix.Timebase.Messages.Service
{
    
    
    [OldElementName(Value="deltix.qsrv.hf.pub.EventMessage")]
    [SchemaElement(Name="com.epam.deltix.timebase.messages.service.EventMessage", Title="Event Message", Description=null)]
    public class EventMessage : InstrumentMessage
    {
        
        private EventMessageType _eventType = ((EventMessageType)(TypeConstants.EnumNull));
        
        /// <summary>
        /// Creates an instance of EventMessage object.
        /// </summary>
        public EventMessage()
        {
        }
        
        #region Properties
        [SchemaElement(Name="eventType", Title="Event Type", Description=null)]
        public virtual EventMessageType EventType
        {
            get
            {
                return this._eventType;
            }
            set
            {
                this._eventType = value;
            }
        }
        #endregion
        
        #region Property Helpers
        /// <summary>
        /// Flag that indicates whether property EventType has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasEventType()
        {
            return (this._eventType != ((EventMessageType)(TypeConstants.EnumNull)));
        }
        
        /// <summary>
        /// Sets null to EventType property.
        /// </summary>
        public void NullifyEventType()
        {
            this._eventType = ((EventMessageType)(TypeConstants.EnumNull));
        }
        #endregion
        
        #region Standard Methods
        /// <summary>
        /// Determines whether the specified object is equal to the current object.
        /// <param name="obj">The object to compare with the current object.</param>
        /// <returns>true if the specified object is equal to the current object; otherwise, false.</returns>
        /// </summary>
        public virtual bool Equals(EventMessage obj)
        {
            if ((obj == null))
            {
                return false;
            }
            if ((this.EventType != obj.EventType))
            {
                return false;
            }
            return base.Equals(obj);
        }
        
        /// <summary>
        /// Determines whether the specified object is equal to the current object.
        /// <param name="obj">The object to compare with the current object.</param>
        /// <returns>true if the specified object is equal to the current object; otherwise, false.</returns>
        /// </summary>
        public override bool Equals(object obj)
        {
            if ((typeof(EventMessage).IsInstanceOfType(obj) == false))
            {
                return false;
            }
            return this.Equals(((EventMessage)(obj)));
        }
        
        /// <summary>
        /// Calculates a hash code for the current object.
        /// <returns>A hash code for the current object.</returns>
        /// </summary>
        public override int GetHashCode()
        {
            unchecked
            {
            int hash = ((int)(2166136261u));
            hash = ((hash * 16777619) 
                        + this.EventType.GetHashCode());
            return ((hash * 16777619) 
                        + base.GetHashCode());
            }
        }
        
        /// <summary>
        /// Appends an object state to a given StringBuilder in a form of JSON.
        /// <returns>A StringBuilder that was used to append the object to.</returns>
        /// </summary>
        public override System.Text.StringBuilder ToString(System.Text.StringBuilder builder)
        {
            builder.Append("{ \"$type\":  \"EventMessage\"");
            if (this.HasEventType())
            {
                builder.Append(", \"EventType\": \"");
                builder.Append(this.EventType);
                builder.Append("\"");
            }
            if (this.HasTimeStampMs())
            {
                builder.Append(", \"TimeStampMs\": \"");
                builder.Append(this.TimeStampMs);
                builder.Append("\"");
            }
            if (this.HasNanoTime())
            {
                builder.Append(", \"NanoTime\": \"");
                builder.Append(this.NanoTime);
                builder.Append("\"");
            }
            if (this.HasSymbol())
            {
                builder.Append(", \"Symbol\": \"");
                builder.Append(this.Symbol);
                builder.Append("\"");
            }
            builder.Append("}");
            return builder;
        }
        
        /// <summary>
        /// Returns a string that represents the current object.
        /// <returns>A string that represents the current object.</returns>
        /// </summary>
        public override string ToString()
        {
            return this.ToString(new System.Text.StringBuilder()).ToString();
        }
        #endregion
    }
}