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
    /// Class which defines a change to schema descriptor.
    /// </summary>
    public interface ISchemaDescriptorChangeActionInterface : ISchemaDescriptorChangeActionInfo, IRecordInterface
    {
        
        #region Properties
        /// <summary>
        /// Previous descriptor state.
        /// </summary>
        new IClassDescriptorInterface PreviousState
        {
            get;
            set;
        }
        
        /// <summary>
        /// New descriptor state.
        /// </summary>
        new IClassDescriptorInterface NewState
        {
            get;
            set;
        }
        
        /// <summary>
        /// Bitmask that defines the changes that were applied to the descriptor.
        /// </summary>
        new SchemaDescriptorChangeType ChangeTypes
        {
            get;
            set;
        }
        
        /// <summary>
        /// Defines the data transformation that was applied to the descriptor.
        /// </summary>
        new ISchemaDescriptorTransformationInterface DescriptorTransformation
        {
            get;
            set;
        }
        
        /// <summary>
        /// A list of change actions to data fields.
        /// Populated only if ChangeTypes contains ALTER flag
        /// </summary>
        new IList<ISchemaFieldChangeActionInterface> FieldChangeActions
        {
            get;
            set;
        }
        #endregion
        
        #region Methods
        /// <summary>
        /// Sets null to PreviousState property.
        /// </summary>
        void NullifyPreviousState();
        
        /// <summary>
        /// Sets null to NewState property.
        /// </summary>
        void NullifyNewState();
        
        /// <summary>
        /// Sets null to ChangeTypes property.
        /// </summary>
        void NullifyChangeTypes();
        
        /// <summary>
        /// Sets null to DescriptorTransformation property.
        /// </summary>
        void NullifyDescriptorTransformation();
        
        /// <summary>
        /// Sets null to FieldChangeActions property.
        /// </summary>
        void NullifyFieldChangeActions();
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        new ISchemaDescriptorChangeActionInterface Clone();
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        new ISchemaDescriptorChangeActionInterface CopyFrom(IRecordInfo source);
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        new ISchemaDescriptorChangeActionInterface Nullify();
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        new ISchemaDescriptorChangeActionInterface Reset();
        #endregion
    }
}
