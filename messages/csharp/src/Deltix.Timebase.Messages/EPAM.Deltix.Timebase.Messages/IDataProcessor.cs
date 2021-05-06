using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace EPAM.Deltix.Timebase.Messages{
	/// <summary>
	/// Interface for data processor. We can send data to this processor and it will return processed data to us.
	/// </summary>
	public interface IDataProcessor
	{
		/// <summary>
		/// Send message with data which will be processed.
		/// Note: FlyWeight pattern in use. We don't keep any references on your classes (message) after method returns execution.
		/// </summary>
		/// <param name="message">Message to processed.</param>
		void SendPackage(IMessageInfo message);
		/// <summary>
		/// Callback which will be called after processing to the end of listener's list.
		/// Note: FlyWeight pattern in use. Objects received through event could be reused after listener returns execution.
		/// </summary>
		event Action<IMessageInfo> OnEndTransformation;
	}
}
