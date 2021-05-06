using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EPAM.Deltix.Timebase.Messages{
	public interface IRecordInterface : IRecordInfo
	{
		IRecordInterface CopyFrom(IRecordInfo readOnlyRecord);
		IRecordInterface Nullify();
		IRecordInterface Reset();
		new IRecordInterface Clone();
	}
}
