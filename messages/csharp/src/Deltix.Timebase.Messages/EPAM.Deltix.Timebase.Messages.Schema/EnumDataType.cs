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
    /// Schema definition of enum data type.
    /// </summary>
    [SchemaElement(Name="com.epam.deltix.timebase.messages.schema.EnumDataType", Title="EnumDataType", Description=null)]
    public class EnumDataType : DataType, IEnumDataTypeInterface
    {
        
        public new static string ClassName = "com.epam.deltix.timebase.messages.schema.EnumDataType";
        
        private IClassDescriptorRefInterface _typeDescriptor = null;
        
        /// <summary>
        /// Creates an instance of EnumDataType object.
        /// </summary>
        public EnumDataType()
        {
        }
        
        #region Properties
        /// <summary>
        /// Definition for enumeration class.
        /// </summary>
        [SchemaIgnore()]
        IClassDescriptorRefInfo IEnumDataTypeInfo.TypeDescriptor
        {
            get
            {
                return this._typeDescriptor;
            }
        }
        
        /// <summary>
        /// Definition for enumeration class.
        /// </summary>
        [SchemaElement(Name=null, Title=null, Description=null)]
        [SchemaType(Encoding=null, IsNullable=false, DataType=SchemaDataType.Object, Minimum=null, Maximum=null, NestedTypes=new Type[] {
                typeof(ClassDescriptorRef)})]
        public virtual IClassDescriptorRefInterface TypeDescriptor
        {
            get
            {
                return this._typeDescriptor;
            }
            set
            {
                this._typeDescriptor = value;
            }
        }
        #endregion
        
        #region Property Helpers
        /// <summary>
        /// Flag that indicates whether property TypeDescriptor has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasTypeDescriptor()
        {
            return (this._typeDescriptor != null);
        }
        
        /// <summary>
        /// Sets null to TypeDescriptor property.
        /// </summary>
        public void NullifyTypeDescriptor()
        {
            this._typeDescriptor = null;
        }
        #endregion
        
        #region Message Methods
        /// <summary>
        /// Creates a new instance of message.
        /// <returns>New instance of message of the same type as this.</returns>
        /// </summary>
        protected override IRecordInterface CreateInstance()
        {
            return new EnumDataType();
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        IEnumDataTypeInfo IEnumDataTypeInfo.Clone()
        {
            return ((IEnumDataTypeInfo)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        IEnumDataTypeInterface IEnumDataTypeInterface.Clone()
        {
            return ((IEnumDataTypeInterface)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        public new EnumDataType Clone()
        {
            return ((EnumDataType)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        protected override IRecordInterface CopyFromImpl(IRecordInfo source)
        {
            base.CopyFromImpl(source);
            if (typeof(IEnumDataTypeInfo).IsInstanceOfType(source))
            {
                IEnumDataTypeInfo typedSource = ((IEnumDataTypeInfo)(source));
                if ((typedSource.TypeDescriptor != null))
                {
                    if (((this.TypeDescriptor == null) 
                                || (this.TypeDescriptor.GetType() == typedSource.TypeDescriptor.GetType())))
                    {
                        this.TypeDescriptor = ((ClassDescriptorRef)(typedSource.TypeDescriptor.Clone()));
                    }
                    else
                    {
                        this.TypeDescriptor.CopyFrom(typedSource.TypeDescriptor);
                    }
                }
                else
                {
                    this.TypeDescriptor = null;
                }
            }
            return this;
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        IEnumDataTypeInterface IEnumDataTypeInterface.CopyFrom(IRecordInfo source)
        {
            return ((IEnumDataTypeInterface)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        public new EnumDataType CopyFrom(IRecordInfo source)
        {
            return ((EnumDataType)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        protected override IRecordInterface NullifyImpl()
        {
            base.NullifyImpl();
            this.NullifyTypeDescriptor();
            return this;
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        IEnumDataTypeInterface IEnumDataTypeInterface.Nullify()
        {
            return ((IEnumDataTypeInterface)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        public new EnumDataType Nullify()
        {
            return ((EnumDataType)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        protected override IRecordInterface ResetImpl()
        {
            base.ResetImpl();
            this._typeDescriptor = null;
            return this;
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        IEnumDataTypeInterface IEnumDataTypeInterface.Reset()
        {
            return ((IEnumDataTypeInterface)(this.ResetImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        public new EnumDataType Reset()
        {
            return ((EnumDataType)(this.ResetImpl()));
        }
        #endregion
        
        #region Standard Methods
        /// <summary>
        /// Determines whether the specified object is equal to the current object.
        /// <param name="obj">The object to compare with the current object.</param>
        /// <returns>true if the specified object is equal to the current object; otherwise, false.</returns>
        /// </summary>
        public virtual bool Equals(EnumDataType obj)
        {
            if ((obj == null))
            {
                return false;
            }
            if (((this.TypeDescriptor != null) 
                        && (obj.TypeDescriptor != null)))
            {
                if ((this.TypeDescriptor.Equals(obj.TypeDescriptor) != true))
                {
                    return false;
                }
            }
            else
            {
                if ((this.TypeDescriptor != obj.TypeDescriptor))
                {
                    return false;
                }
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
            if ((typeof(EnumDataType).IsInstanceOfType(obj) == false))
            {
                return false;
            }
            return this.Equals(((EnumDataType)(obj)));
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
            if ((this.TypeDescriptor == null))
            {
                hash = ((hash * 16777619) 
                            + 0);
            }
            else
            {
                hash = ((hash * 16777619) 
                            + this.TypeDescriptor.GetHashCode());
            }
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
            builder.Append("{ \"$type\":  \"EnumDataType\"");
            if (this.HasTypeDescriptor())
            {
                builder.Append(", \"TypeDescriptor\": {");
                this.TypeDescriptor.ToString(builder);
                builder.Append("}");
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