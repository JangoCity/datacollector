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
package com.streamsets.datacollector.config;

public class DataRuleDefinition extends RuleDefinition {

  private final String label;
  private final String lane;
  private final double samplingPercentage;
  private final int samplingRecordsToRetain;

  /*alert related options*/
  private final boolean alertEnabled;
  private final ThresholdType thresholdType;
  private final String thresholdValue;
  private final long minVolume;

  /*create a meter to indicate rate of records matching the condition over time.*/
  private final boolean meterEnabled;

  public DataRuleDefinition(String id, String label, String lane, double samplingPercentage,
                            int samplingRecordsToRetain, String condition, boolean alertEnabled, String alertText,
                            ThresholdType thresholdType, String thresholdValue, long minVolume, boolean meterEnabled,
                            boolean sendEmail, boolean enabled) {
    super(id, condition, alertText, sendEmail, enabled);
    this.label = label;
    this.lane = lane;
    this.samplingPercentage = samplingPercentage;
    this.samplingRecordsToRetain = samplingRecordsToRetain;
    this.alertEnabled = alertEnabled;
    this.thresholdType = thresholdType;
    this.thresholdValue = thresholdValue;
    this.minVolume = minVolume;
    this.meterEnabled = meterEnabled;
  }

  public String getLabel() {
    return label;
  }

  public String getLane() {
    return lane;
  }

  public double getSamplingPercentage() {
    return samplingPercentage;
  }

  public int getSamplingRecordsToRetain() {
    return samplingRecordsToRetain;
  }

  public boolean isAlertEnabled() {
    return alertEnabled;
  }

  public ThresholdType getThresholdType() {
    return thresholdType;
  }

  public String getThresholdValue() {
    return thresholdValue;
  }

  public long getMinVolume() {
    return minVolume;
  }

  public boolean isMeterEnabled() {
    return meterEnabled;
  }

}
