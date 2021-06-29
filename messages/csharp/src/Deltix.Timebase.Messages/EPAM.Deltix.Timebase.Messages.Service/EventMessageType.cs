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


namespace EPAM.Deltix.Timebase.Messages.Service
{
    
    
    [OldElementName(Value="deltix.qsrv.hf.pub.EventMessage$EventType")]
    [SchemaElement(Name="com.epam.deltix.timebase.messages.service.EventMessageType", Title="Event Type", Description=null)]
    public enum EventMessageType
    {
        
        [SchemaElement(Name="STREAM_DELETED", Title=null, Description=null)]
        StreamDeleted = 0,
        
        [SchemaElement(Name="STREAM_CREATED", Title=null, Description=null)]
        StreamCreated = 1,
        
        [SchemaElement(Name="READ_LOCK_ACQUIRED", Title=null, Description=null)]
        ReadLockAcquired = 2,
        
        [SchemaElement(Name="READ_LOCK_RELEASED", Title=null, Description=null)]
        ReadLockReleased = 3,
        
        [SchemaElement(Name="WRITE_LOCK_ACQUIRED", Title=null, Description=null)]
        WriteLockAcquired = 4,
        
        [SchemaElement(Name="WRITE_LOCK_RELEASED", Title=null, Description=null)]
        WriteLockReleased = 5,
    }
}