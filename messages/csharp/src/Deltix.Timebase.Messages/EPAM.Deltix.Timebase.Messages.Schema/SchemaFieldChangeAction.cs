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
    /// Class which defines a change to schema field.
    /// </summary>
    [SchemaElement(Name="com.epam.deltix.timebase.messages.schema.SchemaFieldChangeAction", Title="SchemaFieldChangeAction", Description=null)]
    public class SchemaFieldChangeAction : ISchemaFieldChangeActionInterface, IRecordInterface, ICloneable
    {
        
        public static string ClassName = "com.epam.deltix.timebase.messages.schema.SchemaFieldChangeAction";
        
        private IDataFieldInterface _previousState = null;
        
        private IDataFieldInterface _newState = null;
        
        private SchemaFieldChangeType _changeTypes = ((SchemaFieldChangeType)(TypeConstants.EnumNull));
        
        private ISchemaFieldDataTransformationInterface _dataTransformation = null;
        
        /// <summary>
        /// Creates an instance of SchemaFieldChangeAction object.
        /// </summary>
        public SchemaFieldChangeAction()
        {
        }
        
        #region Properties
        /// <summary>
        /// Previous data field state.
        /// </summary>
        [SchemaIgnore()]
        IDataFieldInfo ISchemaFieldChangeActionInfo.PreviousState
        {
            get
            {
                return this._previousState;
            }
        }
        
        /// <summary>
        /// Previous data field state.
        /// </summary>
        [SchemaElement(Name=null, Title=null, Description=null)]
        [SchemaType(Encoding=null, IsNullable=true, DataType=SchemaDataType.Object, Minimum=null, Maximum=null, NestedTypes=new Type[] {
                typeof(StaticDataField),
                typeof(NonStaticDataField)})]
        public virtual IDataFieldInterface PreviousState
        {
            get
            {
                return this._previousState;
            }
            set
            {
                this._previousState = value;
            }
        }
        
        /// <summary>
        /// New descriptor state.
        /// </summary>
        [SchemaIgnore()]
        IDataFieldInfo ISchemaFieldChangeActionInfo.NewState
        {
            get
            {
                return this._newState;
            }
        }
        
        /// <summary>
        /// New descriptor state.
        /// </summary>
        [SchemaElement(Name=null, Title=null, Description=null)]
        [SchemaType(Encoding=null, IsNullable=true, DataType=SchemaDataType.Object, Minimum=null, Maximum=null, NestedTypes=new Type[] {
                typeof(StaticDataField),
                typeof(NonStaticDataField)})]
        public virtual IDataFieldInterface NewState
        {
            get
            {
                return this._newState;
            }
            set
            {
                this._newState = value;
            }
        }
        
        /// <summary>
        /// Bitmask that defines the changes that were applied to the field.
        /// </summary>
        [SchemaElement(Name=null, Title=null, Description=null)]
        [SchemaType(Encoding=null, IsNullable=false, DataType=SchemaDataType.Default, Minimum=null, Maximum=null, NestedTypes=new Type[0])]
        public virtual SchemaFieldChangeType ChangeTypes
        {
            get
            {
                return this._changeTypes;
            }
            set
            {
                this._changeTypes = value;
            }
        }
        
        /// <summary>
        /// Defines the data transformation that was applied to the field.
        /// </summary>
        [SchemaIgnore()]
        ISchemaFieldDataTransformationInfo ISchemaFieldChangeActionInfo.DataTransformation
        {
            get
            {
                return this._dataTransformation;
            }
        }
        
        /// <summary>
        /// Defines the data transformation that was applied to the field.
        /// </summary>
        [SchemaElement(Name=null, Title=null, Description=null)]
        [SchemaType(Encoding=null, IsNullable=true, DataType=SchemaDataType.Object, Minimum=null, Maximum=null, NestedTypes=new Type[] {
                typeof(SchemaFieldDataTransformation)})]
        public virtual ISchemaFieldDataTransformationInterface DataTransformation
        {
            get
            {
                return this._dataTransformation;
            }
            set
            {
                this._dataTransformation = value;
            }
        }
        #endregion
        
        #region Property Helpers
        /// <summary>
        /// Flag that indicates whether property PreviousState has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasPreviousState()
        {
            return (this._previousState != null);
        }
        
        /// <summary>
        /// Sets null to PreviousState property.
        /// </summary>
        public void NullifyPreviousState()
        {
            this._previousState = null;
        }
        
        /// <summary>
        /// Flag that indicates whether property NewState has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasNewState()
        {
            return (this._newState != null);
        }
        
        /// <summary>
        /// Sets null to NewState property.
        /// </summary>
        public void NullifyNewState()
        {
            this._newState = null;
        }
        
        /// <summary>
        /// Flag that indicates whether property ChangeTypes has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasChangeTypes()
        {
            return (this._changeTypes != ((SchemaFieldChangeType)(TypeConstants.EnumNull)));
        }
        
        /// <summary>
        /// Sets null to ChangeTypes property.
        /// </summary>
        public void NullifyChangeTypes()
        {
            this._changeTypes = ((SchemaFieldChangeType)(TypeConstants.EnumNull));
        }
        
        /// <summary>
        /// Flag that indicates whether property DataTransformation has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasDataTransformation()
        {
            return (this._dataTransformation != null);
        }
        
        /// <summary>
        /// Sets null to DataTransformation property.
        /// </summary>
        public void NullifyDataTransformation()
        {
            this._dataTransformation = null;
        }
        #endregion
        
        #region Message Methods
        /// <summary>
        /// Creates a new instance of message.
        /// <returns>New instance of message of the same type as this.</returns>
        /// </summary>
        protected virtual IRecordInterface CreateInstance()
        {
            return new SchemaFieldChangeAction();
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
        ISchemaFieldChangeActionInfo ISchemaFieldChangeActionInfo.Clone()
        {
            return ((ISchemaFieldChangeActionInfo)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        ISchemaFieldChangeActionInterface ISchemaFieldChangeActionInterface.Clone()
        {
            return ((ISchemaFieldChangeActionInterface)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        public SchemaFieldChangeAction Clone()
        {
            return ((SchemaFieldChangeAction)(this.CloneImpl()));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        protected virtual IRecordInterface CopyFromImpl(IRecordInfo source)
        {
            if (typeof(ISchemaFieldChangeActionInfo).IsInstanceOfType(source))
            {
                ISchemaFieldChangeActionInfo typedSource = ((ISchemaFieldChangeActionInfo)(source));
                if ((typedSource.PreviousState != null))
                {
                    if (((this.PreviousState == null) 
                                || (this.PreviousState.GetType() == typedSource.PreviousState.GetType())))
                    {
                        this.PreviousState = ((DataField)(typedSource.PreviousState.Clone()));
                    }
                    else
                    {
                        this.PreviousState.CopyFrom(typedSource.PreviousState);
                    }
                }
                else
                {
                    this.PreviousState = null;
                }
                if ((typedSource.NewState != null))
                {
                    if (((this.NewState == null) 
                                || (this.NewState.GetType() == typedSource.NewState.GetType())))
                    {
                        this.NewState = ((DataField)(typedSource.NewState.Clone()));
                    }
                    else
                    {
                        this.NewState.CopyFrom(typedSource.NewState);
                    }
                }
                else
                {
                    this.NewState = null;
                }
                this.ChangeTypes = typedSource.ChangeTypes;
                if ((typedSource.DataTransformation != null))
                {
                    if (((this.DataTransformation == null) 
                                || (this.DataTransformation.GetType() == typedSource.DataTransformation.GetType())))
                    {
                        this.DataTransformation = ((SchemaFieldDataTransformation)(typedSource.DataTransformation.Clone()));
                    }
                    else
                    {
                        this.DataTransformation.CopyFrom(typedSource.DataTransformation);
                    }
                }
                else
                {
                    this.DataTransformation = null;
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
        ISchemaFieldChangeActionInterface ISchemaFieldChangeActionInterface.CopyFrom(IRecordInfo source)
        {
            return ((ISchemaFieldChangeActionInterface)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        public SchemaFieldChangeAction CopyFrom(IRecordInfo source)
        {
            return ((SchemaFieldChangeAction)(this.CopyFromImpl(source)));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        protected virtual IRecordInterface NullifyImpl()
        {
            this.NullifyPreviousState();
            this.NullifyNewState();
            this.NullifyChangeTypes();
            this.NullifyDataTransformation();
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
        ISchemaFieldChangeActionInterface ISchemaFieldChangeActionInterface.Nullify()
        {
            return ((ISchemaFieldChangeActionInterface)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        public SchemaFieldChangeAction Nullify()
        {
            return ((SchemaFieldChangeAction)(this.NullifyImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        protected virtual IRecordInterface ResetImpl()
        {
            this._previousState = null;
            this._newState = null;
            this._changeTypes = ((SchemaFieldChangeType)(TypeConstants.EnumNull));
            this._dataTransformation = null;
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
        ISchemaFieldChangeActionInterface ISchemaFieldChangeActionInterface.Reset()
        {
            return ((ISchemaFieldChangeActionInterface)(this.ResetImpl()));
        }
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        public SchemaFieldChangeAction Reset()
        {
            return ((SchemaFieldChangeAction)(this.ResetImpl()));
        }
        #endregion
        
        #region Standard Methods
        /// <summary>
        /// Determines whether the specified object is equal to the current object.
        /// <param name="obj">The object to compare with the current object.</param>
        /// <returns>true if the specified object is equal to the current object; otherwise, false.</returns>
        /// </summary>
        public virtual bool Equals(SchemaFieldChangeAction obj)
        {
            if ((obj == null))
            {
                return false;
            }
            if (((this.PreviousState != null) 
                        && (obj.PreviousState != null)))
            {
                if ((this.PreviousState.Equals(obj.PreviousState) != true))
                {
                    return false;
                }
            }
            else
            {
                if ((this.PreviousState != obj.PreviousState))
                {
                    return false;
                }
            }
            if (((this.NewState != null) 
                        && (obj.NewState != null)))
            {
                if ((this.NewState.Equals(obj.NewState) != true))
                {
                    return false;
                }
            }
            else
            {
                if ((this.NewState != obj.NewState))
                {
                    return false;
                }
            }
            if ((this.ChangeTypes != obj.ChangeTypes))
            {
                return false;
            }
            if (((this.DataTransformation != null) 
                        && (obj.DataTransformation != null)))
            {
                if ((this.DataTransformation.Equals(obj.DataTransformation) != true))
                {
                    return false;
                }
            }
            else
            {
                if ((this.DataTransformation != obj.DataTransformation))
                {
                    return false;
                }
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
            if ((typeof(SchemaFieldChangeAction).IsInstanceOfType(obj) == false))
            {
                return false;
            }
            return this.Equals(((SchemaFieldChangeAction)(obj)));
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
            if ((this.PreviousState == null))
            {
                hash = ((hash * 16777619) 
                            + 0);
            }
            else
            {
                hash = ((hash * 16777619) 
                            + this.PreviousState.GetHashCode());
            }
            if ((this.NewState == null))
            {
                hash = ((hash * 16777619) 
                            + 0);
            }
            else
            {
                hash = ((hash * 16777619) 
                            + this.NewState.GetHashCode());
            }
            hash = ((hash * 16777619) 
                        + this.ChangeTypes.GetHashCode());
            if ((this.DataTransformation == null))
            {
                hash = ((hash * 16777619) 
                            + 0);
            }
            else
            {
                hash = ((hash * 16777619) 
                            + this.DataTransformation.GetHashCode());
            }
            return hash;
            }
        }
        
        /// <summary>
        /// Appends an object state to a given StringBuilder in a form of JSON.
        /// <returns>A StringBuilder that was used to append the object to.</returns>
        /// </summary>
        public virtual System.Text.StringBuilder ToString(System.Text.StringBuilder builder)
        {
            builder.Append("{ \"$type\":  \"SchemaFieldChangeAction\"");
            if (this.HasPreviousState())
            {
                builder.Append(", \"PreviousState\": {");
                this.PreviousState.ToString(builder);
                builder.Append("}");
            }
            if (this.HasNewState())
            {
                builder.Append(", \"NewState\": {");
                this.NewState.ToString(builder);
                builder.Append("}");
            }
            if (this.HasChangeTypes())
            {
                builder.Append(", \"ChangeTypes\": \"");
                builder.Append(this.ChangeTypes);
                builder.Append("\"");
            }
            if (this.HasDataTransformation())
            {
                builder.Append(", \"DataTransformation\": {");
                this.DataTransformation.ToString(builder);
                builder.Append("}");
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
