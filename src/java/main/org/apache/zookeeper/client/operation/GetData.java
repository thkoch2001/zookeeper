/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.zookeeper.client.operation;

import org.apache.jute.Record;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.proto.GetDataRequest;
import org.apache.zookeeper.proto.GetDataResponse;
import org.apache.zookeeper.client.WatchRegistration;

public class GetData extends Operation {
	private Watcher watcher = null;
	private byte[] data;
	private Stat stat;
	
	public GetData(String path) {
		super(path);
	}
	
	public GetData(String path, Watcher watcher) {
		this(path);
		this.watcher = watcher;
	}

	@Override
	public Record createRequest() {		
		GetDataRequest request = new GetDataRequest();
		request.setPath(path);
		request.setWatch(watcher != null);
		
		return request;
	}

	@Override
	public Record createResponse() {
		return new GetDataResponse();
	}

	@Override
	public void receiveResponse(Record response) {
		GetDataResponse getDataResponse = (GetDataResponse)response;
		
		data = getDataResponse.getData();
		stat = getDataResponse.getStat();
	}

	@Override
	public int getRequestOpCode() {
		return ZooDefs.OpCode.getData;
	}

	public byte[] getData() {
		return data;
	}
	
	@Override
	public WatchRegistration.Data getWatchRegistration() {
		if(watcher != null) {
			return new WatchRegistration.Data(watcher, path);
		}
		return null;	
	}

	public Stat getStat() {
		return stat;
	}
}
