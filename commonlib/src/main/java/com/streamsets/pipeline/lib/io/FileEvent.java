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
package com.streamsets.pipeline.lib.io;

import com.streamsets.pipeline.api.impl.Utils;

/**
 * File events generated by the {@link com.streamsets.pipeline.lib.io.MultiFileReader} when starting and ending reading a file.
 */
public class FileEvent {

  public enum Action { START, END, ERROR}

  private final LiveFile file;
  private final Action action;

  FileEvent(LiveFile file, Action action) {
    this.file = Utils.checkNotNull(file, "file");
    this.action = Utils.checkNotNull(action, "action");
  }

  /**
   * Indicates if its a file start reading event.
   *
   * @return <code>true</code> if starting the file, <code>false</code> otherwise.
   */
  public Action getAction() {
    return action;
  }

  /**
   * Returns the <code>LiveFile</code> triggering the event.
   *
   * @return the <code>LiveFile</code> triggering the event.
   */
  public LiveFile getFile() {
    return file;
  }

  public String toString() {
    return Utils.format("MultiFileReader.Event[file='{}' action='{}'", file, action);
  }

  @Override
  public int hashCode() {
    return file.hashCode() + action.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj instanceof FileEvent) {
      FileEvent other = (FileEvent) obj;
      return file.equals(other.file) && action == other.action;
    } else {
      return false;
    }
  }
}
