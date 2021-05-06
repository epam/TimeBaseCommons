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


namespace EPAM.Deltix.Timebase.Messages.Service
{
    
    
    [OldElementName(Value="deltix.qsrv.hf.pub.BinaryMessage")]
    [SchemaElement(Name="com.epam.deltix.timebase.messages.service.BinaryMessage", Title="Binary Message", Description=null)]
    [SchemaGuid(Value=BinaryMessage.DescriptorGuid)]
    public class BinaryMessage : InstrumentMessage
    {
        
        public const String DescriptorGuid = "SYS:BinaryMessage:1";
        
        private IBinaryArrayReadOnly _data = null;
        
        /// <summary>
        /// Creates an instance of BinaryMessage object.
        /// </summary>
        public BinaryMessage()
        {
        }
        
        #region Properties
        [SchemaElement(Name="data", Title="Data buffer", Description=null)]
        [SchemaType(Encoding=null, IsNullable=true, DataType=SchemaDataType.Binary, Minimum=null, Maximum=null, NestedTypes=new Type[0])]
        public virtual IBinaryArrayReadOnly Data
        {
            get
            {
                return this._data;
            }
            set
            {
                this._data = value;
            }
        }
        #endregion
        
        #region Property Helpers
        /// <summary>
        /// Flag that indicates whether property Data has an assigned value.
        /// <returns>true if property has an assigned value, false otherwise.</returns>
        /// </summary>
        public bool HasData()
        {
            return (this._data != null);
        }
        
        /// <summary>
        /// Sets null to Data property.
        /// </summary>
        public void NullifyData()
        {
            this._data = null;
        }
        #endregion
        
        #region Standard Methods
        /// <summary>
        /// Determines whether the specified object is equal to the current object.
        /// <param name="obj">The object to compare with the current object.</param>
        /// <returns>true if the specified object is equal to the current object; otherwise, false.</returns>
        /// </summary>
        public virtual bool Equals(BinaryMessage obj)
        {
            if ((obj == null))
            {
                return false;
            }
            if (((this.Data != null) 
                        && (obj.Data != null)))
            {
                if ((this.Data.Equals(obj.Data) != true))
                {
                    return false;
                }
            }
            else
            {
                if ((this.Data != obj.Data))
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
            if ((typeof(BinaryMessage).IsInstanceOfType(obj) == false))
            {
                return false;
            }
            return this.Equals(((BinaryMessage)(obj)));
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
            if ((this.Data == null))
            {
                hash = ((hash * 16777619) 
                            + 0);
            }
            else
            {
                hash = ((hash * 16777619) 
                            + this.Data.GetHashCode());
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
            builder.Append("{ \"$type\":  \"BinaryMessage\"");
            if (this.HasData())
            {
                builder.Append(", \"Data\": \"");
                builder.Append(this.Data);
                builder.Append("\"");
            }
            if (this.HasTimeStampMs())
            {
                builder.Append(", \"TimeStampMs\": \"");
                builder.Append(this.TimeStampMs);
                builder.Append("\"");
            }
            if (this.HasNanoTime())
            {
                builder.Append(", \"NanoTime\": \"");
                builder.Append(this.NanoTime);
                builder.Append("\"");
            }
            if (this.HasSymbol())
            {
                builder.Append(", \"Symbol\": \"");
                builder.Append(this.Symbol);
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
