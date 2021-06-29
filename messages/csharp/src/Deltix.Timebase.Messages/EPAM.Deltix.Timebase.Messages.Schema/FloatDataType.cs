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
    /// Schema definition of float data type.
    /// </summary>
    [SchemaElement(Name="com.epam.deltix.timebase.messages.schema.FloatDataType", Title="FloatDataType", Description=null)]
    public class FloatDataType : IntegralDataType, IFloatDataTypeInterface
    {
        
        public new static string ClassName = "com.epam.deltix.timebase.messages.schema.FloatDataType";
        
        private UInt16 _scale = UInt16.MinValue;
        
        /// <summary>
        /// Creates an instance of FloatDataType object.
        /// </summary>
        public FloatDataType()
        {
        }
        
        #region Properties
        /// <summary>
        /// Scale.
        /// </summary>
        [SchemaElement(Name=null, Title=null, Description=null)]
        [SchemaType(Encoding=null, IsNullable=true, DataType=SchemaDataType.Default, Minimum=null, Maximum=null, NestedTypes=new Type[0])]
        public virtual UInt16 Scale
        {
            get
            {
                return this._scale;
            }
            set
            {
                this._scale = value;
            }
        }
        #endregion
        
        #region Property Helpers
        /// <summary>
        /// Flag that indicates whether property Scale has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasScale()
        {
            return (this._scale != UInt16.MinValue);
        }
        
        /// <summary>
        /// Sets null to Scale property.
        /// </summary>
        public void NullifyScale()
        {
            this._scale = UInt16.MinValue;
        }
        #endregion
        
        #region Message Methods
        /// <summary>
        /// Creates a new instance of message.
        /// <returns>New instance of message of the same type as this.</returns>
        /// </summary>
        protected override IRecordInterface CreateInstance()
        {
            return new FloatDataType();
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        IFloatDataTypeInfo IFloatDataTypeInfo.Clone()
        {
            return ((IFloatDataTypeInfo)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        IFloatDataTypeInterface IFloatDataTypeInterface.Clone()
        {
            return ((IFloatDataTypeInterface)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        public new FloatDataType Clone()
        {
            return ((FloatDataType)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        protected override IRecordInterface CopyFromImpl(IRecordInfo source)
        {
            base.CopyFromImpl(source);
            if (typeof(IFloatDataTypeInfo).IsInstanceOfType(source))
            {
                IFloatDataTypeInfo typedSource = ((IFloatDataTypeInfo)(source));
                this.Scale = typedSource.Scale;
            }
            return this;
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        IFloatDataTypeInterface IFloatDataTypeInterface.CopyFrom(IRecordInfo source)
        {
            return ((IFloatDataTypeInterface)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        public new FloatDataType CopyFrom(IRecordInfo source)
        {
            return ((FloatDataType)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        protected override IRecordInterface NullifyImpl()
        {
            base.NullifyImpl();
            this.NullifyScale();
            return this;
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        IFloatDataTypeInterface IFloatDataTypeInterface.Nullify()
        {
            return ((IFloatDataTypeInterface)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        public new FloatDataType Nullify()
        {
            return ((FloatDataType)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        protected override IRecordInterface ResetImpl()
        {
            base.ResetImpl();
            this._scale = UInt16.MinValue;
            return this;
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        IFloatDataTypeInterface IFloatDataTypeInterface.Reset()
        {
            return ((IFloatDataTypeInterface)(this.ResetImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        public new FloatDataType Reset()
        {
            return ((FloatDataType)(this.ResetImpl()));
        }
        #endregion
        
        #region Standard Methods
        /// <summary>
        /// Determines whether the specified object is equal to the current object.
        /// <param name="obj">The object to compare with the current object.</param>
        /// <returns>true if the specified object is equal to the current object; otherwise, false.</returns>
        /// </summary>
        public virtual bool Equals(FloatDataType obj)
        {
            if ((obj == null))
            {
                return false;
            }
            if ((this.Scale != obj.Scale))
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
            if ((typeof(FloatDataType).IsInstanceOfType(obj) == false))
            {
                return false;
            }
            return this.Equals(((FloatDataType)(obj)));
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
                        + this.Scale.GetHashCode());
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
            builder.Append("{ \"$type\":  \"FloatDataType\"");
            if (this.HasScale())
            {
                builder.Append(", \"Scale\": ");
                builder.Append(this.Scale);
            }
            if (this.HasMinValue())
            {
                builder.Append(", \"MinValue\": \"");
                builder.Append(this.MinValue);
                builder.Append("\"");
            }
            if (this.HasMaxValue())
            {
                builder.Append(", \"MaxValue\": \"");
                builder.Append(this.MaxValue);
                builder.Append("\"");
            }
            if (this.HasEncoding())
            {
                builder.Append(", \"Encoding\": \"");
                builder.Append(this.Encoding);
                builder.Append("\"");
            }
            if (this.HasIsNullable())
            {
                builder.Append(", \"IsNullable\": ");
                builder.Append(this.IsNullable);
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