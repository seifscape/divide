/*
 * Copyright (C) 2014 Divide.io
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

package io.divide.dao.appengine;

import io.divide.shared.transitory.TransientObject;

import java.util.Map;

public class BackendToOfy{

    private BackendToOfy(){}

    public static OfyObject getOfy(TransientObject transientObject) {
        OfyObject oo = new OfyObject(transientObject.getObjectKey(),transientObject.getUserData(),transientObject.getMetaData());
        return oo;
    }

    public static TransientObject getBack(OfyObject ofyObject) {
        TempObject beo = new TempObject(); // gonna get over written anyways
        beo.setMaps(ofyObject.user_data,ofyObject.meta_data);
        return beo;
    }

    private static class TempObject extends TransientObject{

        public void setMaps(Map<String,Object> userData, Map<String,String> metaData){
            this.user_data = userData;
            this.meta_data = metaData;
        }
    }

}