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
    /// Schema definition of binary data type.
    /// </summary>
    public interface IBinaryDataTypeInterface : IBinaryDataTypeInfo, IDataTypeInterface
    {
        
        #region Properties
        /// <summary>
        /// Maximum field length in bytes.
        /// </summary>
        new UInt32 MaxSize
        {
            get;
            set;
        }
        
        /// <summary>
        /// Compression level for binary data.
        /// </summary>
        new UInt16 CompressionLevel
        {
            get;
            set;
        }
        #endregion
        
        #region Methods
        /// <summary>
        /// Sets null to MaxSize property.
        /// </summary>
        void NullifyMaxSize();
        
        /// <summary>
        /// Sets null to CompressionLevel property.
        /// </summary>
        void NullifyCompressionLevel();
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        new IBinaryDataTypeInterface Clone();
        
        /// <summary>
        /// Deep copies content from src instance to this.
        /// <param name="source">Source for copy.</param>
        /// <returns>this.</returns>
        /// </summary>
        new IBinaryDataTypeInterface CopyFrom(IRecordInfo source);
        
        /// <summary>
        /// Set null to all fields of this instance.
        /// <returns>this.</returns>
        /// </summary>
        new IBinaryDataTypeInterface Nullify();
        
        /// <summary>
        /// Reset all instance field to their default states.
        /// <returns>this.</returns>
        /// </summary>
        new IBinaryDataTypeInterface Reset();
        #endregion
    }
}
