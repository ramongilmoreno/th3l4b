package com.th3l4b.srm.model.base;

import com.th3l4b.common.data.named.IContainer;
import com.th3l4b.common.data.named.INamed;
import com.th3l4b.common.data.propertied.IPropertied;

public interface IEntity extends INamed, IPropertied, IContainer<IField> {
}
