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
    /// Schema definition for enumeration class.
    /// </summary>
    public interface IEnumClassDescriptorInfo : IClassDescriptorInfo
    {
        
        #region Properties
        /// <summary>
        /// List of values of enumeration.
        /// </summary>
        IReadOnlyList<IEnumValueInfo> Values
        {
            get;
        }
        
        /// <summary>
        /// True, if enumeration represents a bitmask.
        /// </summary>
        Boolean IsBitmask
        {
            get;
        }
        #endregion
        
        #region Methods
        /// <summary>
        /// Flag that indicates whether property Values has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        Boolean HasValues();
        
        /// <summary>
        /// Flag that indicates whether property IsBitmask has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        Boolean HasIsBitmask();
        
        /// <summary>
        /// Creates copy of this instance.
        /// <returns>Copy.</returns>
        /// </summary>
        new IEnumClassDescriptorInfo Clone();
        #endregion
    }
}
