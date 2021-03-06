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
    /// Class which defines a transformation that is applied to the descriptor.
    /// </summary>
    [SchemaElement(Name="com.epam.deltix.timebase.messages.schema.SchemaDescriptorTransformation", Title="SchemaDescriptorTransformation", Description=null)]
    public class SchemaDescriptorTransformation : ISchemaDescriptorTransformationInterface, IRecordInterface, ICloneable
    {
        
        public static string ClassName = "com.epam.deltix.timebase.messages.schema.SchemaDescriptorTransformation";
        
        private SchemaDescriptorTransformationType _transformationType = ((SchemaDescriptorTransformationType)(TypeConstants.EnumNull));
        
        /// <summary>
        /// Creates an instance of SchemaDescriptorTransformation object.
        /// </summary>
        public SchemaDescriptorTransformation()
        {
        }
        
        #region Properties
        /// <summary>
        /// Defines the transformation type that was applied to the descriptor.
        /// </summary>
        [SchemaElement(Name=null, Title=null, Description=null)]
        [SchemaType(Encoding=null, IsNullable=false, DataType=SchemaDataType.Default, Minimum=null, Maximum=null, NestedTypes=new Type[0])]
        public virtual SchemaDescriptorTransformationType TransformationType
        {
            get
            {
                return this._transformationType;
            }
            set
            {
                this._transformationType = value;
            }
        }
        #endregion
        
        #region Property Helpers
        /// <summary>
        /// Flag that indicates whether property TransformationType has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasTransformationType()
        {
            return (this._transformationType != ((SchemaDescriptorTransformationType)(TypeConstants.EnumNull)));
        }
        
        /// <summary>
        /// Sets null to TransformationType property.
        /// </summary>
        public void NullifyTransformationType()
        {
            this._transformationType = ((SchemaDescriptorTransformationType)(TypeConstants.EnumNull));
        }
        #endregion
        
        #region Message Methods
        /// <summary>
        /// Creates a new instance of message.
        /// <returns>New instance of message of the same type as this.</returns>
        /// </summary>
        protected virtual IRecordInterface CreateInstance()
        {
            return new SchemaDescriptorTransformation();
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
        ISchemaDescriptorTransformationInfo ISchemaDescriptorTransformationInfo.Clone()
        {
            return ((ISchemaDescriptorTransformationInfo)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        ISchemaDescriptorTransformationInterface ISchemaDescriptorTransformationInterface.Clone()
        {
            return ((ISchemaDescriptorTransformationInterface)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        public SchemaDescriptorTransformation Clone()
        {
            return ((SchemaDescriptorTransformation)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        protected virtual IRecordInterface CopyFromImpl(IRecordInfo source)
        {
            if (typeof(ISchemaDescriptorTransformationInfo).IsInstanceOfType(source))
            {
                ISchemaDescriptorTransformationInfo typedSource = ((ISchemaDescriptorTransformationInfo)(source));
                this.TransformationType = typedSource.TransformationType;
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
        ISchemaDescriptorTransformationInterface ISchemaDescriptorTransformationInterface.CopyFrom(IRecordInfo source)
        {
            return ((ISchemaDescriptorTransformationInterface)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        public SchemaDescriptorTransformation CopyFrom(IRecordInfo source)
        {
            return ((SchemaDescriptorTransformation)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        protected virtual IRecordInterface NullifyImpl()
        {
            this.NullifyTransformationType();
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
        ISchemaDescriptorTransformationInterface ISchemaDescriptorTransformationInterface.Nullify()
        {
            return ((ISchemaDescriptorTransformationInterface)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        public SchemaDescriptorTransformation Nullify()
        {
            return ((SchemaDescriptorTransformation)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        protected virtual IRecordInterface ResetImpl()
        {
            this._transformationType = ((SchemaDescriptorTransformationType)(TypeConstants.EnumNull));
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
        ISchemaDescriptorTransformationInterface ISchemaDescriptorTransformationInterface.Reset()
        {
            return ((ISchemaDescriptorTransformationInterface)(this.ResetImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        public SchemaDescriptorTransformation Reset()
        {
            return ((SchemaDescriptorTransformation)(this.ResetImpl()));
        }
        #endregion
        
        #region Standard Methods
        /// <summary>
        /// Determines whether the specified object is equal to the current object.
        /// <param name="obj">The object to compare with the current object.</param>
        /// <returns>true if the specified object is equal to the current object; otherwise, false.</returns>
        /// </summary>
        public virtual bool Equals(SchemaDescriptorTransformation obj)
        {
            if ((obj == null))
            {
                return false;
            }
            if ((this.TransformationType != obj.TransformationType))
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
            if ((typeof(SchemaDescriptorTransformation).IsInstanceOfType(obj) == false))
            {
                return false;
            }
            return this.Equals(((SchemaDescriptorTransformation)(obj)));
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
                        + this.TransformationType.GetHashCode());
            return hash;
            }
        }
        
        /// <summary>
        /// Appends an object state to a given StringBuilder in a form of JSON.
        /// <returns>A StringBuilder that was used to append the object to.</returns>
        /// </summary>
        public virtual System.Text.StringBuilder ToString(System.Text.StringBuilder builder)
        {
            builder.Append("{ \"$type\":  \"SchemaDescriptorTransformation\"");
            if (this.HasTransformationType())
            {
                builder.Append(", \"TransformationType\": \"");
                builder.Append(this.TransformationType);
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