using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EPAM.Deltix.Timebase.Messages{
	public interface IRecordInfo
	{
		/// <summary>
		/// Creates copy of this instance.
		/// <returns>Copy.</returns>
		/// </summary>
		IRecordInfo Clone();

		/// <summary>
		/// Creates copy of this instance.
		/// <returns>String representation of this instance</returns>
		/// </summary>
		StringBuilder ToString(StringBuilder builder);
	}
}
