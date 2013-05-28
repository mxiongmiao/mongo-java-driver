/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mongodb.operation.protocol;

import org.mongodb.Document;
import org.mongodb.Encoder;
import org.mongodb.connection.ChannelAwareOutputBuffer;
import org.mongodb.operation.BaseUpdate;

public abstract class BaseUpdateMessage extends RequestMessage {
    private final Encoder<Document> baseEncoder;


    public BaseUpdateMessage(final String collectionName, final OpCode opCode, final Encoder<Document> encoder,
                             final MessageSettings settings) {
        super(collectionName, opCode, settings);
        this.baseEncoder = encoder;
    }

    protected void writeBaseUpdate(final ChannelAwareOutputBuffer buffer) {
        buffer.writeInt(0); // reserved
        buffer.writeCString(getCollectionName());

        int flags = 0;
        if (getUpdateBase().isUpsert()) {
            flags |= 1;
        }
        if (getUpdateBase().isMulti()) {
            flags |= 2;
        }
        buffer.writeInt(flags);

        addDocument(getUpdateBase().getFilter(), baseEncoder, buffer);
    }

    protected abstract BaseUpdate getUpdateBase();

    public Encoder<Document> getBaseEncoder() {
        return baseEncoder;
    }
}