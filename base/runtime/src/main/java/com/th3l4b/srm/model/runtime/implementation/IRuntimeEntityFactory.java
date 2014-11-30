package com.th3l4b.srm.model.runtime.implementation;

import com.th3l4b.srm.model.runtime.IRuntimeEntity;

public interface IRuntimeEntityFactory {
	IRuntimeEntity create () throws Exception;
}
