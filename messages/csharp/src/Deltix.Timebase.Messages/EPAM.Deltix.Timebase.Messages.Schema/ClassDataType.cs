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
using System.Collections.Generic;


namespace EPAM.Deltix.Timebase.Messages.Schema
{
    
    
    /// <summary>
    /// Schema definition of class data type.
    /// </summary>
    [SchemaElement(Name="com.epam.deltix.timebase.messages.schema.ClassDataType", Title="ClassDataType", Description=null)]
    public class ClassDataType : DataType, IClassDataTypeInterface
    {
        
        public new static string ClassName = "com.epam.deltix.timebase.messages.schema.ClassDataType";
        
        private IList<IClassDescriptorRefInterface> _typeDescriptors = null;
        
        /// <summary>
        /// Creates an instance of ClassDataType object.
        /// </summary>
        public ClassDataType()
        {
        }
        
        #region Properties
        /// <summary>
        /// Definitions for nested schema classes.
        /// </summary>
        [SchemaIgnore()]
        IReadOnlyList<IClassDescriptorRefInfo> IClassDataTypeInfo.TypeDescriptors
        {
            get
            {
                return ((IReadOnlyList<IClassDescriptorRefInterface>)(this._typeDescriptors));
            }
        }
        
        /// <summary>
        /// Definitions for nested schema classes.
        /// </summary>
        [SchemaElement(Name=null, Title=null, Description=null)]
        [SchemaArrayType(IsNullable=false, IsElementNullable=false, ElementEncoding=null, ElementDataType=SchemaDataType.Default, ElementMinimum=null, ElementMaximum=null, ElementTypes=new Type[] {
                typeof(ClassDescriptorRef)})]
        public virtual IList<IClassDescriptorRefInterface> TypeDescriptors
        {
            get
            {
                return this._typeDescriptors;
            }
            set
            {
                if (((value == null) 
                            || (typeof(IReadOnlyList<IClassDescriptorRefInterface>).IsInstanceOfType(value) == true)))
                {
                    this._typeDescriptors = value;
                }
                else
                {
                    throw new System.InvalidOperationException("Assigned value must implement IReadOnlyList<IClassDescriptorRefInterface> interfa" +
                            "ce.");
                }
            }
        }
        #endregion
        
        #region Property Helpers
        /// <summary>
        /// Flag that indicates whether property TypeDescriptors has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasTypeDescriptors()
        {
            return (this._typeDescriptors != null);
        }
        
        /// <summary>
        /// Sets null to TypeDescriptors property.
        /// </summary>
        public void NullifyTypeDescriptors()
        {
            this._typeDescriptors = null;
        }
        #endregion
        
        #region Message Methods
        /// <summary>
        /// Creates a new instance of message.
        /// <returns>New instance of message of the same type as this.</returns>
        /// </summary>
        protected override IRecordInterface CreateInstance()
        {
            return new ClassDataType();
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        IClassDataTypeInfo IClassDataTypeInfo.Clone()
        {
            return ((IClassDataTypeInfo)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        IClassDataTypeInterface IClassDataTypeInterface.Clone()
        {
            return ((IClassDataTypeInterface)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        public new ClassDataType Clone()
        {
            return ((ClassDataType)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        protected override IRecordInterface CopyFromImpl(IRecordInfo source)
        {
            base.CopyFromImpl(source);
            if (typeof(IClassDataTypeInfo).IsInstanceOfType(source))
            {
                IClassDataTypeInfo typedSource = ((IClassDataTypeInfo)(source));
                if ((typedSource.TypeDescriptors != null))
                {
                    this.TypeDescriptors = new List<IClassDescriptorRefInterface>(typedSource.TypeDescriptors.Count);
                    int i;
                    for (i = 0; (i < typedSource.TypeDescriptors.Count); i = (i + 1))
                    {
                        IClassDescriptorRefInfo item = typedSource.TypeDescriptors[i];
                        if ((item == null))
                        {
                            this.TypeDescriptors.Add(null);
                        }
                        else
                        {
                            this.TypeDescriptors.Add(((IClassDescriptorRefInterface)(item.Clone())));
                        }
                    }
                }
                else
                {
                    this.TypeDescriptors = null;
                }
            }
            return this;
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        IClassDataTypeInterface IClassDataTypeInterface.CopyFrom(IRecordInfo source)
        {
            return ((IClassDataTypeInterface)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        public new ClassDataType CopyFrom(IRecordInfo source)
        {
            return ((ClassDataType)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        protected override IRecordInterface NullifyImpl()
        {
            base.NullifyImpl();
            this.NullifyTypeDescriptors();
            return this;
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        IClassDataTypeInterface IClassDataTypeInterface.Nullify()
        {
            return ((IClassDataTypeInterface)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        public new ClassDataType Nullify()
        {
            return ((ClassDataType)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        protected override IRecordInterface ResetImpl()
        {
            base.ResetImpl();
            this._typeDescriptors = null;
            return this;
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        IClassDataTypeInterface IClassDataTypeInterface.Reset()
        {
            return ((IClassDataTypeInterface)(this.ResetImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        public new ClassDataType Reset()
        {
            return ((ClassDataType)(this.ResetImpl()));
        }
        #endregion
        
        #region Standard Methods
        /// <summary>
        /// Determines whether the specified object is equal to the current object.
        /// <param name="obj">The object to compare with the current object.</param>
        /// <returns>true if the specified object is equal to the current object; otherwise, false.</returns>
        /// </summary>
        public virtual bool Equals(ClassDataType obj)
        {
            if ((obj == null))
            {
                return false;
            }
            if (((this.TypeDescriptors != null) 
                        && (obj.TypeDescriptors != null)))
            {
                if ((this.TypeDescriptors.Count != obj.TypeDescriptors.Count))
                {
                    return false;
                }
                int i;
                for (i = 0; (i < obj.TypeDescriptors.Count); i = (i + 1))
                {
                    if (((this.TypeDescriptors[i] != null) 
                                && (obj.TypeDescriptors[i] != null)))
                    {
                        if ((this.TypeDescriptors[i].Equals(obj.TypeDescriptors[i]) != true))
                        {
                            return false;
                        }
                    }
                    else
                    {
                        if ((this.TypeDescriptors[i] != obj.TypeDescriptors[i]))
                        {
                            return false;
                        }
                    }
                }
            }
            else
            {
                if ((this.TypeDescriptors != obj.TypeDescriptors))
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
            if ((typeof(ClassDataType).IsInstanceOfType(obj) == false))
            {
                return false;
            }
            return this.Equals(((ClassDataType)(obj)));
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
            if ((this.TypeDescriptors == null))
            {
                hash = ((hash * 16777619) 
                            + 0);
            }
            else
            {
                int i;
                for (i = 0; (i < this.TypeDescriptors.Count); i = (i + 1))
                {
                    if ((this.TypeDescriptors[i] == null))
                    {
                        hash = ((hash * 16777619) 
                                    + 0);
                    }
                    else
                    {
                        hash = ((hash * 16777619) 
                                    + this.TypeDescriptors[i].GetHashCode());
                    }
                }
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
            builder.Append("{ \"$type\":  \"ClassDataType\"");
            if (this.HasTypeDescriptors())
            {
                builder.Append(", \"TypeDescriptors\": [");
                int i;
                for (i = 0; (i < this.TypeDescriptors.Count); i = (i + 1))
                {
                    if ((i != 0))
                    {
                        builder.Append(",");
                    }
                    this.TypeDescriptors[i].ToString(builder);
                }
                builder.Append("]");
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