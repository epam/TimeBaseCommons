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
    /// Schema definition of integer data type.
    /// </summary>
    [SchemaElement(Name="com.epam.deltix.timebase.messages.schema.IntegerDataType", Title="IntegerDataType", Description=null)]
    public class IntegerDataType : IntegralDataType, IIntegerDataTypeInterface
    {
        
        public new static string ClassName = "com.epam.deltix.timebase.messages.schema.IntegerDataType";
        
        /// <summary>
        /// Creates an instance of IntegerDataType object.
        /// </summary>
        public IntegerDataType()
        {
        }
        
        #region Message Methods
        /// <summary>
        /// Creates a new instance of message.
        /// <returns>New instance of message of the same type as this.</returns>
        /// </summary>
        protected override IRecordInterface CreateInstance()
        {
            return new IntegerDataType();
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        IIntegerDataTypeInfo IIntegerDataTypeInfo.Clone()
        {
            return ((IIntegerDataTypeInfo)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        IIntegerDataTypeInterface IIntegerDataTypeInterface.Clone()
        {
            return ((IIntegerDataTypeInterface)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        public new IntegerDataType Clone()
        {
            return ((IntegerDataType)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        protected override IRecordInterface CopyFromImpl(IRecordInfo source)
        {
            base.CopyFromImpl(source);
            return this;
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        IIntegerDataTypeInterface IIntegerDataTypeInterface.CopyFrom(IRecordInfo source)
        {
            return ((IIntegerDataTypeInterface)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        public new IntegerDataType CopyFrom(IRecordInfo source)
        {
            return ((IntegerDataType)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        protected override IRecordInterface NullifyImpl()
        {
            base.NullifyImpl();
            return this;
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        IIntegerDataTypeInterface IIntegerDataTypeInterface.Nullify()
        {
            return ((IIntegerDataTypeInterface)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        public new IntegerDataType Nullify()
        {
            return ((IntegerDataType)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        protected override IRecordInterface ResetImpl()
        {
            base.ResetImpl();
            return this;
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        IIntegerDataTypeInterface IIntegerDataTypeInterface.Reset()
        {
            return ((IIntegerDataTypeInterface)(this.ResetImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        public new IntegerDataType Reset()
        {
            return ((IntegerDataType)(this.ResetImpl()));
        }
        #endregion
        
        #region Standard Methods
        /// <summary>
        /// Determines whether the specified object is equal to the current object.
        /// <param name="obj">The object to compare with the current object.</param>
        /// <returns>true if the specified object is equal to the current object; otherwise, false.</returns>
        /// </summary>
        public virtual bool Equals(IntegerDataType obj)
        {
            if ((obj == null))
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
            if ((typeof(IntegerDataType).IsInstanceOfType(obj) == false))
            {
                return false;
            }
            return this.Equals(((IntegerDataType)(obj)));
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
            builder.Append("{ \"$type\":  \"IntegerDataType\"");
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