package com.th3l4b.srm.sync.server.persistence;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.th3l4b.srm.json.Generator;
import com.th3l4b.srm.json.IJsonModelRuntime;
import com.th3l4b.srm.json.JsonUtils;
import com.th3l4b.srm.json.Parser;
import com.th3l4b.srm.json.Parser.Result;
import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.model.runtime.IRuntime;
import com.th3l4b.srm.sync.server.generated.ServerSyncModelUtils;
import com.th3l4b.srm.sync.server.generated.entities.IClient;
import com.th3l4b.srm.sync.server.generated.entities.IMerge;
import com.th3l4b.srm.sync.server.generated.entities.IStatus;
import com.th3l4b.srm.sync.server.persistence.actions.IMoveToOtherStatusAction;
import com.th3l4b.srm.sync.server.persistence.actions.INewStatusAction;

/**
 * @see ServerSyncModelUtils
 */
public class SRMBasedSyncServerPersistence implements ISyncServerPersistence {

	private IRuntime _repositoryRuntime;
	private ServerSyncModelUtils _utils;
	private IModelRuntime _modelRuntime;

	public SRMBasedSyncServerPersistence(IRuntime repositoryRuntime,
			IModelRuntime modelRuntime) throws Exception {
		_repositoryRuntime = repositoryRuntime;
		_modelRuntime = modelRuntime;
		_utils = new ServerSyncModelUtils(_repositoryRuntime);
	}

	@Override
	public Collection<String> liveStatuses() throws Exception {
		HashSet<String> r = new HashSet<String>();
		for (IStatus s : _utils.finder().allStatus()) {
			r.add(s.coordinates().getIdentifier().getKey());
		}
		return r;
	}

	@Override
	public String newStatus(INewStatusAction action) throws Exception {
		ArrayList<IInstance> updates = new ArrayList<IInstance>();

		IStatus s = _utils.createStatus();
		Collection<IInstance> u = action.updates();
		if (u.size() > 0) {
			IJsonModelRuntime jr = JsonUtils.runtime(_modelRuntime);
			StringWriter sw = new StringWriter();
			Generator g = new Generator(jr, sw);
			try {
				g.write(u);
			} finally {
				g.close();
			}
			s.setContents(sw.getBuffer().toString());
		}
		updates.add(s);

		IClient c = _utils.createClient();
		c.coordinates().getIdentifier().setKey(action.client());
		c.coordinates().setStatus(EntityStatus.ToSave);
		c.setStatus(s);
		updates.add(c);

		String sid = s.coordinates().getIdentifier().getKey();
		for (String ms : action.statusesForNewStatus()) {
			IMerge m = _utils.createMerge();
			m.setFrom(ms);
			m.setTo(sid);
			updates.add(m);
		}

		for (String dsi : action.statusesToDelete()) {
			IStatus ds = _utils.createStatus();
			ds.coordinates().getIdentifier().setKey(dsi);
			ds.coordinates().setStatus(EntityStatus.ToDelete);
			updates.add(ds);
		}

		_utils.update(updates);

		return sid;
	}

	@Override
	public void moveToOtherStatus(IMoveToOtherStatusAction action)
			throws Exception {
		ArrayList<IInstance> updates = new ArrayList<IInstance>();
		IClient c = _utils.createClient();
		c.coordinates().getIdentifier().setKey(action.client());
		c.setStatus(action.status());
		updates.add(c);

		for (String dsi : action.statusesToDelete()) {
			IStatus ds = _utils.createStatus();
			ds.coordinates().getIdentifier().setKey(dsi);
			ds.coordinates().setStatus(EntityStatus.ToDelete);
			updates.add(ds);
		}
		_utils.update(updates);
	}

	@Override
	public String createClient(String id) throws Exception {
		IStatus s = _utils.createStatus();
		IClient c = _utils.createClient();
		c.coordinates().getIdentifier().setKey(id);
		c.coordinates().setStatus(EntityStatus.ToSave);
		c.setStatus(s);
		_utils.update(Arrays.asList(new IInstance[] { c, s }));
		return s.coordinates().getIdentifier().getKey();
	}

	@Override
	public String clientStatus(String id) throws Exception {
		IClient c = _utils.finder().findClient(id);
		if (c.coordinates().getStatus() == EntityStatus.Unknown) {
			return null;
		} else {
			return c.getStatus();
		}
	}

	@Override
	public Collection<String> statusesThisStatusDependsOn(String to)
			throws Exception {
		HashSet<String> r = new HashSet<String>();
		for (IMerge m : _utils.finder().referencesStatus_ComesFrom(to)) {
			r.add(m.getFrom());
		}
		return r;
	}

	@Override
	public Collection<String> statusesThatDependsOnThisStatus(String from)
			throws Exception {
		HashSet<String> r = new HashSet<String>();
		for (IMerge m : _utils.finder().referencesStatus_LeadsTo(from)) {
			r.add(m.getTo());
		}
		return r;
	}

	@Override
	public Collection<IInstance> updates(String status) throws Exception {
		String contents = _utils.finder().findStatus(status).getContents();
		if (contents != null) {
			StringReader sr = new StringReader(contents);
			IJsonModelRuntime jr = JsonUtils.runtime(_modelRuntime);
			Parser parser = new Parser(jr, sr);
			Result r = parser.parse(false, true);
			parser.close();
			return r._many;
		}
		return Collections.emptyList();
	}

	@Override
	public IModelRuntime modelRuntime() throws Exception {
		return _modelRuntime;
	}
}
