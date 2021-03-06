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
    
    
    /// <summary>
    /// Special transient message that signals active stream consumers that their stream metadata has been changed
    /// Used for advanced stream monitoring.
    /// see also "SelectionOptions.VersionTracking"
    /// </summary>
    [OldElementName(Value="deltix.qsrv.hf.pub.MetaDataChangeMessage")]
    [SchemaElement(Name="com.epam.deltix.timebase.messages.service.MetaDataChangeMessage", Title="Meta Data Change Message", Description=null)]
    public class MetaDataChangeMessage : SystemMessage
    {
        
        private SByte _converted = TypeConstants.BooleanNull;
        
        private Int64 _version = TypeConstants.Int64Null;
        
        /// <summary>
        /// Creates an instance of MetaDataChangeMessage object.
        /// </summary>
        public MetaDataChangeMessage()
        {
        }
        
        #region Properties
        /// <summary>
        /// Indicates, that stream data was converted.
        /// </summary>
        [SchemaElement(Name="converted", Title="Converted", Description=null)]
        public virtual Boolean Converted
        {
            get
            {
                if ((this._converted == TypeConstants.BooleanTrue))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            set
            {
                if ((value == true))
                {
                    this._converted = TypeConstants.BooleanTrue;
                }
                else
                {
                    this._converted = TypeConstants.BooleanFalse;
                }
            }
        }
        
        [SchemaElement(Name="version", Title="Version", Description=null)]
        public virtual Int64 Version
        {
            get
            {
                return this._version;
            }
            set
            {
                this._version = value;
            }
        }
        #endregion
        
        #region Property Helpers
        /// <summary>
        /// Flag that indicates whether property Converted has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasConverted()
        {
            return (this._converted != TypeConstants.BooleanNull);
        }
        
        /// <summary>
        /// Sets null to Converted property.
        /// </summary>
        public void NullifyConverted()
        {
            this._converted = TypeConstants.BooleanNull;
        }
        
        /// <summary>
        /// Flag that indicates whether property Version has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasVersion()
        {
            return (this._version != TypeConstants.Int64Null);
        }
        
        /// <summary>
        /// Sets null to Version property.
        /// </summary>
        public void NullifyVersion()
        {
            this._version = TypeConstants.Int64Null;
        }
        #endregion
        
        #region Standard Methods
        /// <summary>
        /// Determines whether the specified object is equal to the current object.
        /// <param name="obj">The object to compare with the current object.</param>
        /// <returns>true if the specified object is equal to the current object; otherwise, false.</returns>
        /// </summary>
        public virtual bool Equals(MetaDataChangeMessage obj)
        {
            if ((obj == null))
            {
                return false;
            }
            if (((this.HasConverted() != obj.HasConverted()) 
                        || (this.Converted != obj.Converted)))
            {
                return false;
            }
            if ((this.Version != obj.Version))
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
            if ((typeof(MetaDataChangeMessage).IsInstanceOfType(obj) == false))
            {
                return false;
            }
            return this.Equals(((MetaDataChangeMessage)(obj)));
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
                        + this.Converted.GetHashCode());
            hash = ((hash * 16777619) 
                        + this.Version.GetHashCode());
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
            builder.Append("{ \"$type\":  \"MetaDataChangeMessage\"");
            if (this.HasConverted())
            {
                builder.Append(", \"Converted\": ");
                builder.Append(this.Converted);
            }
            if (this.HasVersion())
            {
                builder.Append(", \"Version\": ");
                builder.Append(this.Version);
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