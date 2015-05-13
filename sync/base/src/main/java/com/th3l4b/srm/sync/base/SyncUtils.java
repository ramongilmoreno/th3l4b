package com.th3l4b.srm.sync.base;

import java.util.Collection;
import java.util.HashMap;

import com.th3l4b.srm.model.runtime.EntityStatus;
import com.th3l4b.srm.model.runtime.ICoordinates;
import com.th3l4b.srm.model.runtime.IEntityRuntime;
import com.th3l4b.srm.model.runtime.IIdentifier;
import com.th3l4b.srm.model.runtime.IInstance;
import com.th3l4b.srm.model.runtime.IModelRuntime;

public class SyncUtils {

	/**
	 * Groups multiples updates into a single one.
	 * 
	 * @param input
	 *            The list of updates. May contain more than one update for the
	 *            same entity.
	 * @return The list of updates, grouped by entity. No more than an update
	 *         per entity may exist in this result.
	 */
	public static Collection<IInstance> groupUpdates(
			Collection<IInstance> input, IModelRuntime mr) throws Exception {
		HashMap<IIdentifier, IInstance> r = new HashMap<IIdentifier, IInstance>();
		for (IInstance instance : input) {
			ICoordinates coordinates = instance.coordinates();
			EntityStatus status = coordinates.getStatus();
			if (!status.isAction()) {
				continue;
			}
			IIdentifier id = coordinates.getIdentifier();
			IEntityRuntime er = mr.get(id.getType());
			if (!r.containsKey(id)) {
				IInstance copy = er.create();
				er.copy(instance, copy);
				r.put(id, copy);
			} else {
				IInstance found = r.get(id);
				EntityStatus initialStatus = found.coordinates().getStatus();

				// Merge
				er.apply(instance, found);

				EntityStatus resultStatus = null;
				if ((status == EntityStatus.ToDelete)
						|| (status == EntityStatus.ToSave)) {
					resultStatus = status;
				} else {
					resultStatus = initialStatus;
				}
				found.coordinates().setStatus(resultStatus);
			}
		}
		return r.values();
	}

	/**
	 * Computes the missing updates from the step1 that are not overwritten by
	 * step2. The updates resulting updates r makes step1 + step2 = step2 + r.
	 */
	public static Collection<IInstance> missingUpdates(
			Collection<IInstance> step1, Collection<IInstance> step2,
			IModelRuntime mr) throws Exception {
		HashMap<IIdentifier, IInstance> r = new HashMap<IIdentifier, IInstance>();
		step1 = groupUpdates(step1, mr);
		step2 = groupUpdates(step2, mr);
		for (IInstance i : step1) {
			ICoordinates coordinates = i.coordinates();
			EntityStatus status = coordinates.getStatus();
			if (!status.isAction()) {
				continue;
			}
			r.put(coordinates.getIdentifier(), i);
		}

		for (IInstance i : step2) {
			IIdentifier id = i.coordinates().getIdentifier();
			ICoordinates coordinates = i.coordinates();
			EntityStatus status2 = coordinates.getStatus();
			if (!status2.isAction()) {
				continue;
			}

			if (r.containsKey(id)) {
				IInstance found = r.get(id);

				// Apply and check are the same
				IEntityRuntime er = mr.get(id.getType());
				IInstance copy = er.create();
				er.copy(found, copy);

				er.subtract(copy, i);

				// Check the step1 + step2 status
				EntityStatus finalStatus = null;
				if ((status2 == EntityStatus.ToSave)
						|| (status2 == EntityStatus.ToDelete)) {
					if (copy.empty()) {
						// If step2 rules status and no change comes from step1,
						// then do not apply any change.
						r.remove(id);
						continue;
					} else {
						finalStatus = status2;
					}
				} else {
					// Second pass just merged status so step1 status rules
					EntityStatus status1 = found.coordinates().getStatus();
					finalStatus = status1;

					// if step1 was also to merge, then nothing is needed.
					// Remove if empty.
					if (status1 == EntityStatus.ToMerge) {
						if (copy.empty()) {
							r.remove(id);
							continue;
						}
					}
				}

				// Set status to final
				copy.coordinates().setStatus(finalStatus);

				// Replace with the copy
				r.put(id, copy);
			}
		}

		return r.values();

	}
}
