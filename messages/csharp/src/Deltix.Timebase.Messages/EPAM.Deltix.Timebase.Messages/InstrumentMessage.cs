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
using com.epam.deltix.containers.;
using System;
using System.Collections.Generic;
using System.Text;

namespace EPAM.Deltix.Timebase.Messages{
	/// <summary>
	/// Base class for all messages that could be written in Timebase.
	/// </summary>
	[Serializable]
	[OldElementName(Value = "deltix.qsrv.hf.pub.InstrumentMessage")]
	[SchemaElement(Name = "deltix.timebase.messages.InstrumentMessage", Title = "Instrument Message", Description = null)]
	public class InstrumentMessage : IMessageInterface, IMessageInfo, IRecordInfo, IRecordInterface, IIdentityKey, IIdentityKeyInterface, ICloneable
	{
		public static string ClassName = "deltix.timebase.messages.InstrumentMessage";

		private HdDateTime _timestamp = TypeConstants.TimestampNull;		

		private string _symbol = "";

		/// <summary>
		/// Time in this field is measured in nanoseconds that passed since January 1, 1970 UTC.
		/// For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
		/// </summary>
		public virtual HdDateTime NanoTime
		{
			get
			{
				return this._timestamp;
			}
			set
			{
				this._timestamp = value;
			}
		}

		/// <summary>
		/// Sets null to Timestamp property.
		/// </summary>
		public void NullifyNanoTime()
		{
			this._timestamp = TypeConstants.TimestampNull;
		}

		public bool HasNanoTime()
		{
			return this._timestamp != (HdDateTime)TypeConstants.TimestampNull;
		}

		/// <summary>
		/// Time in this field is measured in milliseconds that passed since January 1, 1970 UTC.
		/// For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
		/// </summary>

		public DateTime TimeStampMs
		{
			get { return _timestamp.Timestamp;  }
			set { _timestamp = new HdDateTime(value); }
		}

		/// <summary>
		/// Instrument name.
		/// </summary>
		public string Symbol
		{
			get
			{
				return this._symbol;
			}
			set
			{
				this._symbol = value;
			}
		}

		/// <summary>
		/// Flag that indicates whether property Timestamp has an assigned value.
		/// <returns>true if property has an assigned value, false otherwise.</returns>
		/// </summary>
		public bool HasTimeStampMs()
		{
			return this._timestamp != (HdDateTime)TypeConstants.TimestampNull;
		}

		/// <summary>
		/// Sets null to Timestamp property.
		/// </summary>
		public void NullifyTimeStampMs()
		{
			this._timestamp = TypeConstants.TimestampNull;
		}

		/// <summary>
		/// Flag that indicates whether property Symbol has an assigned value.
		/// <returns>true if property has an assigned value, false otherwise.</returns>
		/// </summary>
		public bool HasSymbol()
		{
			return this._symbol != null;
		}

		/// <summary>
		/// Sets null to Symbol property.
		/// </summary>
		public void NullifySymbol()
		{
			this._symbol = null;
		}

		/// <summary>
		/// Creates a new instance of message.
		/// <returns>New instance of message of the same type as this.</returns>
		/// </summary>
		protected virtual IRecordInterface CreateInstance()
		{
			return new InstrumentMessage();
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
		object ICloneable.Clone()
		{
			return this.CloneImpl();
		}

		/// <summary>
		/// Creates copy of this instance.
		/// <returns>Copy.</returns>
		/// </summary>
		IRecordInterface IRecordInterface.Clone()
		{
			return (IRecordInterface)this.CloneImpl();
		}

		/// <summary>
		/// Creates copy of this instance.
		/// <returns>Copy.</returns>
		/// </summary>
		IRecordInfo IRecordInfo.Clone()
		{
			return this.CloneImpl();
		}

		/// <summary>
		/// Creates copy of this instance.
		/// <returns>Copy.</returns>
		/// </summary>
		IMessageInfo IMessageInfo.Clone()
		{
			return (IMessageInfo)this.CloneImpl();
		}

		/// <summary>
		/// Creates copy of this instance.
		/// <returns>Copy.</returns>
		/// </summary>
		IMessageInterface IMessageInterface.Clone()
		{
			return (IMessageInterface)this.CloneImpl();
		}

		/// <summary>
		/// Creates copy of this instance.
		/// <returns>Copy.</returns>
		/// </summary>
		public InstrumentMessage Clone()
		{
			return (InstrumentMessage)this.CloneImpl();
		}

		/// <summary>
		/// Deep copies content from src instance to this.
		/// <param name="source">Source for copy.</param>
		/// <returns>this.</returns>
		/// </summary>
		protected virtual IRecordInterface CopyFromImpl(IRecordInfo source)
		{
			if (typeof(IMessageInfo).IsInstanceOfType(source))
			{
				IMessageInfo typedSource = (IMessageInfo)source;
				this.NanoTime = typedSource.NanoTime;				
				this.Symbol = typedSource.Symbol;
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
			return this.CopyFromImpl(source);
		}

		/// <summary>
		/// Deep copies content from src instance to this.
		/// <param name="source">Source for copy.</param>
		/// <returns>this.</returns>
		/// </summary>
		IMessageInterface IMessageInterface.CopyFrom(IRecordInfo source)
		{
			return (IMessageInterface)this.CopyFromImpl(source);
		}

		/// <summary>
		/// Deep copies content from src instance to this.
		/// <param name="source">Source for copy.</param>
		/// <returns>this.</returns>
		/// </summary>
		public InstrumentMessage CopyFrom(IRecordInfo source)
		{
			return (InstrumentMessage)this.CopyFromImpl(source);
		}

		/// <summary>
		/// Set null to all fields of this instance.
		/// <returns>this.</returns>
		/// </summary>
		protected virtual IRecordInterface NullifyImpl()
		{
			this.NullifyTimeStampMs();			
			this.NullifySymbol();
			return this;
		}

		/// <summary>
		/// Set null to all fields of this instance.
		/// <returns>this.</returns>
		/// </summary>
		IRecordInterface IRecordInterface.Nullify()
		{
			return this.NullifyImpl();
		}

		/// <summary>
		/// Set null to all fields of this instance.
		/// <returns>this.</returns>
		/// </summary>
		IMessageInterface IMessageInterface.Nullify()
		{
			return (IMessageInterface)this.NullifyImpl();
		}

		/// <summary>
		/// Set null to all fields of this instance.
		/// <returns>this.</returns>
		/// </summary>
		public InstrumentMessage Nullify()
		{
			return (InstrumentMessage)this.NullifyImpl();
		}

		/// <summary>
		/// Reset all instance field to their default states.
		/// <returns>this.</returns>
		/// </summary>
		protected virtual IRecordInterface ResetImpl()
		{
			this._timestamp = TypeConstants.TimestampNull;
			this._symbol = "";
			return this;
		}

		/// <summary>
		/// Reset all instance field to their default states.
		/// <returns>this.</returns>
		/// </summary>
		IRecordInterface IRecordInterface.Reset()
		{
			return this.ResetImpl();
		}

		/// <summary>
		/// Reset all instance field to their default states.
		/// <returns>this.</returns>
		/// </summary>
		IMessageInterface IMessageInterface.Reset()
		{
			return (IMessageInterface)this.ResetImpl();
		}

		/// <summary>
		/// Reset all instance field to their default states.
		/// <returns>this.</returns>
		/// </summary>
		public InstrumentMessage Reset()
		{
			return (InstrumentMessage)this.ResetImpl();
		}

		/// <summary>
		/// Determines whether the specified object is equal to the current object.
		/// <param name="obj">The object to compare with the current object.</param>
		/// <returns>true if the specified object is equal to the current object; otherwise, false.</returns>
		/// </summary>
		public virtual bool Equals(InstrumentMessage obj)
		{
			if (obj == null)
			{
				return false;
			}
			if (this.NanoTime != obj.NanoTime)
			{
				return false;
			}			
			if (this.Symbol != obj.Symbol)
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
			if (!typeof(InstrumentMessage).IsInstanceOfType(obj))
			{
				return false;
			}
			return this.Equals((InstrumentMessage)obj);
		}

		/// <summary>
		/// Calculates a hash code for the current object.
		/// <returns>A hash code for the current object.</returns>
		/// </summary>
		public override int GetHashCode()
		{
			int hash2 = -2128831035;
			hash2 = hash2 * 16777619 + this.NanoTime.GetHashCode();
			return hash2 * 16777619 + this.Symbol.GetHashCode();
		}

		/// <summary>
		/// Appends an object state to a given StringBuilder in a form of JSON.
		/// <returns>A StringBuilder that was used to append the object to.</returns>
		/// </summary>
		public virtual StringBuilder ToString(StringBuilder builder)
		{
			builder.Append("{ \"$type\":  \"InstrumentMessage\"");
			if (this.HasTimeStampMs())
			{
				builder.Append(", \"Timestamp\": \"");
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
			return this.ToString(new StringBuilder()).ToString();
		}
	}
}