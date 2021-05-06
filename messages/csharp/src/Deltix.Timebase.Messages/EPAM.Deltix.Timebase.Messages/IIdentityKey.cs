using System;
using System.Collections.Generic;
using System.Text;

namespace EPAM.Deltix.Timebase.Messages{
	public interface IIdentityKey
	{
		#region Properties
		/// <summary>
		/// Instrument name.
		/// </summary>
		String Symbol
		{
			get;			
		}
		#endregion
	}
}
