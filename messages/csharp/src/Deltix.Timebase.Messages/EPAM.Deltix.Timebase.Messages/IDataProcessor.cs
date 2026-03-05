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