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
    /// This is a base class for schema data types.
    /// </summary>
    [SchemaElement(Name="com.epam.deltix.timebase.messages.schema.DataType", Title="DataType", Description=null)]
    public abstract class DataType : IDataTypeInterface, IRecordInterface, ICloneable
    {
        
        public static string ClassName = "com.epam.deltix.timebase.messages.schema.DataType";
        
        private MutableString _encoding = null;
        
        private SByte _isNullable = TypeConstants.BooleanNull;
        
        /// <summary>
        /// Creates an instance of DataType object.
        /// </summary>
        public DataType()
        {
        }
        
        #region Properties
        /// <summary>
        /// Binary representation (encoding) of a field in database and protocol.
        /// </summary>
        [SchemaElement(Name=null, Title=null, Description=null)]
        [SchemaType(Encoding=null, IsNullable=true, DataType=SchemaDataType.Default, Minimum=null, Maximum=null, NestedTypes=new Type[0])]
        public virtual MutableString Encoding
        {
            get
            {
                return this._encoding;
            }
            set
            {
                this._encoding = value;
            }
        }
        
        /// <summary>
        /// True, if schema design allows the field to be nullable.
        /// </summary>
        [SchemaElement(Name=null, Title=null, Description=null)]
        [SchemaType(Encoding=null, IsNullable=false, DataType=SchemaDataType.Default, Minimum=null, Maximum=null, NestedTypes=new Type[0])]
        public virtual Boolean IsNullable
        {
            get
            {
                if ((this._isNullable == TypeConstants.BooleanTrue))
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
                    this._isNullable = TypeConstants.BooleanTrue;
                }
                else
                {
                    this._isNullable = TypeConstants.BooleanFalse;
                }
            }
        }
        #endregion
        
        #region Property Helpers
        /// <summary>
        /// Flag that indicates whether property Encoding has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasEncoding()
        {
            return (this._encoding != null);
        }
        
        /// <summary>
        /// Sets null to Encoding property.
        /// </summary>
        public void NullifyEncoding()
        {
            this._encoding = null;
        }
        
        /// <summary>
        /// Flag that indicates whether property IsNullable has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasIsNullable()
        {
            return (this._isNullable != TypeConstants.BooleanNull);
        }
        
        /// <summary>
        /// Sets null to IsNullable property.
        /// </summary>
        public void NullifyIsNullable()
        {
            this._isNullable = TypeConstants.BooleanNull;
        }
        #endregion
        
        #region Message Methods
        /// <summary>
        /// Creates a new instance of message.
        /// <returns>New instance of message of the same type as this.</returns>
        /// </summary>
        protected virtual IRecordInterface CreateInstance()
        {
            throw new System.InvalidOperationException("Cannot create an instance of abstract class.");
        }
        
        /// <summary>
        /// Creates a new instance of message.
        /// <returns>New instance of message of the same type as this.</returns>
        /// </summary>
        protected virtual IRecordInfo CloneImpl()
        {
            IRecordInterface message = this.CreateInstance();
            message.CopyFrom(this);
            return message;
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        object System.ICloneable.Clone()
        {
            return ((object)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        IRecordInterface IRecordInterface.Clone()
        {
            return ((IRecordInterface)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        IRecordInfo IRecordInfo.Clone()
        {
            return ((IRecordInfo)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        IDataTypeInfo IDataTypeInfo.Clone()
        {
            return ((IDataTypeInfo)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        IDataTypeInterface IDataTypeInterface.Clone()
        {
            return ((IDataTypeInterface)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        public DataType Clone()
        {
            return ((DataType)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        protected virtual IRecordInterface CopyFromImpl(IRecordInfo source)
        {
            if (typeof(IDataTypeInfo).IsInstanceOfType(source))
            {
                IDataTypeInfo typedSource = ((IDataTypeInfo)(source));
                if ((typedSource.Encoding != null))
                {
                    if ((this.Encoding == null))
                    {
                        this.Encoding = new com.epam.deltix.containers.MutableString(typedSource.Encoding);
                    }
                    else
                    {
                        this.Encoding.Assign(typedSource.Encoding);
                    }
                }
                else
                {
                    this.Encoding = null;
                }
                if ((typedSource.HasIsNullable() == true))
                {
                    this.IsNullable = typedSource.IsNullable;
                }
                else
                {
                    this.NullifyIsNullable();
                }
            }
            return this;
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        IRecordInterface IRecordInterface.CopyFrom(IRecordInfo source)
        {
            return ((IRecordInterface)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        IDataTypeInterface IDataTypeInterface.CopyFrom(IRecordInfo source)
        {
            return ((IDataTypeInterface)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        public DataType CopyFrom(IRecordInfo source)
        {
            return ((DataType)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        protected virtual IRecordInterface NullifyImpl()
        {
            this.NullifyEncoding();
            this.NullifyIsNullable();
            return this;
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        IRecordInterface IRecordInterface.Nullify()
        {
            return ((IRecordInterface)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        IDataTypeInterface IDataTypeInterface.Nullify()
        {
            return ((IDataTypeInterface)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        public DataType Nullify()
        {
            return ((DataType)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        protected virtual IRecordInterface ResetImpl()
        {
            this._encoding = null;
            this._isNullable = TypeConstants.BooleanNull;
            return this;
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        IRecordInterface IRecordInterface.Reset()
        {
            return ((IRecordInterface)(this.ResetImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        IDataTypeInterface IDataTypeInterface.Reset()
        {
            return ((IDataTypeInterface)(this.ResetImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        public DataType Reset()
        {
            return ((DataType)(this.ResetImpl()));
        }
        #endregion
        
        #region Standard Methods
        /// <summary>
        /// Determines whether the specified object is equal to the current object.
        /// <param name="obj">The object to compare with the current object.</param>
        /// <returns>true if the specified object is equal to the current object; otherwise, false.</returns>
        /// </summary>
        public virtual bool Equals(DataType obj)
        {
            if ((obj == null))
            {
                return false;
            }
            if (((this.Encoding != null) 
                        && (obj.Encoding != null)))
            {
                if ((this.Encoding.Equals(obj.Encoding) != true))
                {
                    return false;
                }
            }
            else
            {
                if ((this.Encoding != obj.Encoding))
                {
                    return false;
                }
            }
            if (((this.HasIsNullable() != obj.HasIsNullable()) 
                        || (this.IsNullable != obj.IsNullable)))
            {
                return false;
            }
            return true;
        }
        
        /// <summary>
        /// Determines whether the specified object is equal to the current object.
        /// <param name="obj">The object to compare with the current object.</param>
        /// <returns>true if the specified object is equal to the current object; otherwise, false.</returns>
        /// </summary>
        public override bool Equals(object obj)
        {
            if ((typeof(DataType).IsInstanceOfType(obj) == false))
            {
                return false;
            }
            return this.Equals(((DataType)(obj)));
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
            if ((this.Encoding == null))
            {
                hash = ((hash * 16777619) 
                            + 0);
            }
            else
            {
                hash = ((hash * 16777619) 
                            + this.Encoding.GetHashCode());
            }
            hash = ((hash * 16777619) 
                        + this.IsNullable.GetHashCode());
            return hash;
            }
        }
        
        /// <summary>
        /// Appends an object state to a given StringBuilder in a form of JSON.
        /// <returns>A StringBuilder that was used to append the object to.</returns>
        /// </summary>
        public virtual System.Text.StringBuilder ToString(System.Text.StringBuilder builder)
        {
            builder.Append("{ \"$type\":  \"DataType\"");
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