/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.time;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter (Periodicity.StringPeriodicityAdapter.class)
public final class Periodicity {

    public static class StringPeriodicityAdapter extends XmlAdapter<String, Periodicity> {
        public Periodicity unmarshal(String v) throws Exception {
            return Periodicity.parse(v);
        }

        public String marshal(Periodicity v) throws Exception {
            return v.toString();
        }
    }

    private final Interval        interval;
    private final Type            type;

    Periodicity(Type type) {
        this.type = type;
        this.interval = null;
    }

    Periodicity(Type type, Interval interval) {
        this.interval = interval;
        this.type = type;
    }

    public Periodicity(Periodicity p) {
        this.type = p.type;
        this.interval = p.interval != null ? Interval.create(p.interval.getNumUnits(), p.interval.getUnit()) : null;
    }

    public Interval         getInterval() {
        return interval;
    }

    public Type             getType() {
        return type;
    }

    public static Periodicity      mkRegular(Interval interval) {
        return new Periodicity(Type.REGULAR, interval);
    }

    public static Periodicity      mkIrregular() {
        return new Periodicity(Type.IRREGULAR);
    }

    public static Periodicity      mkStatic() {
        return new Periodicity(Type.STATIC);
    }

    public static Periodicity   parse(String value) {
        if (value == null || value.contains(String.valueOf(Type.IRREGULAR)))
            return mkIrregular();
        if (value.contains(String.valueOf(Type.STATIC)))
            return mkStatic();

        return mkRegular(Interval.valueOf(value));
    }

    public static Periodicity   parse(Interval value) {
        if (value == null)
            return mkIrregular();
        return parse(value.toString());
    }

    @Override
    public String toString() {
        if (interval == null)
            return String.valueOf(type);

        return interval.toString();
    }

    public String toHumanString() {
        if (interval == null)
            return String.valueOf(type);

        return interval.toHumanString();
    }

    public enum Type {
        REGULAR,
        IRREGULAR,
        STATIC,
    }
}