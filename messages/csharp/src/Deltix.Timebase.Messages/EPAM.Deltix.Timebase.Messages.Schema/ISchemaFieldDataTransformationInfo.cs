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
    /// Class which defines a transformation that is applied to a data field.
    /// </summary>
    public interface ISchemaFieldDataTransformationInfo : IRecordInfo
    {
        
        #region Properties
        /// <summary>
        /// Defines the transformation that was applied to the field.
        /// </summary>
        SchemaFieldDataTransformationType TransformationType
        {
            get;
        }
        
        /// <summary>
        /// Defines the default value if TransformationType equals SET_DEFAULT.
        /// </summary>
        MutableString DefaultValue
        {
            get;
        }
        #endregion
        
        #region Methods
        /// <summary>
        /// Flag that indicates whether property TransformationType has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        Boolean HasTransformationType();
        
        /// <summary>
        /// Flag that indicates whether property DefaultValue has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        Boolean HasDefaultValue();
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        new ISchemaFieldDataTransformationInfo Clone();
        #endregion
    }
}
