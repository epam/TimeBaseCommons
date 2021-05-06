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
    /// Schema definition for a schema node.
    /// </summary>
    public interface IClassDescriptorInterface : IClassDescriptorInfo, INamedDescriptorInterface
    {
        
        #region Properties
        /// <summary>
        /// Optional GUID for this node.
        /// </summary>
        new MutableString Guid
        {
            get;
            set;
        }
        #endregion
        
        #region Methods
        /// <summary>
        /// Sets null to Guid property.
        /// </summary>
        void NullifyGuid();
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        new IClassDescriptorInterface Clone();
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        new IClassDescriptorInterface CopyFrom(IRecordInfo source);
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        new IClassDescriptorInterface Nullify();
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        new IClassDescriptorInterface Reset();
        #endregion
    }
}
