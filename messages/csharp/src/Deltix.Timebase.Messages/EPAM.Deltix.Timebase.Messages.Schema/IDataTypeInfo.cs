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
    public interface IDataTypeInfo : IRecordInfo
    {
        
        #region Properties
        /// <summary>
        /// Binary representation (encoding) of a field in database and protocol.
        /// </summary>
        MutableString Encoding
        {
            get;
        }
        
        /// <summary>
        /// True, if schema design allows the field to be nullable.
        /// </summary>
        Boolean IsNullable
        {
            get;
        }
        #endregion
        
        #region Methods
        /// <summary>
        /// Flag that indicates whether property Encoding has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        Boolean HasEncoding();
        
        /// <summary>
        /// Flag that indicates whether property IsNullable has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        Boolean HasIsNullable();
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        new IDataTypeInfo Clone();
        #endregion
    }
}
