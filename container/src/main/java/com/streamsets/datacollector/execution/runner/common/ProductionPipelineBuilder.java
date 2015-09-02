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
package com.streamsets.datacollector.execution.runner.common;

import com.streamsets.datacollector.config.PipelineConfiguration;
import com.streamsets.datacollector.creation.PipelineBeanCreator;
import com.streamsets.datacollector.creation.PipelineConfigBean;
import com.streamsets.datacollector.main.RuntimeInfo;
import com.streamsets.datacollector.runner.Observer;
import com.streamsets.datacollector.runner.Pipeline;
import com.streamsets.datacollector.runner.PipelineRuntimeException;
import com.streamsets.datacollector.runner.production.ProductionSourceOffsetCommitterOffsetTracker;
import com.streamsets.datacollector.runner.production.ProductionSourceOffsetTracker;
import com.streamsets.datacollector.stagelibrary.StageLibraryTask;
import com.streamsets.datacollector.util.Configuration;
import com.streamsets.datacollector.util.ContainerError;
import com.streamsets.datacollector.util.ValidationUtil;
import com.streamsets.datacollector.validation.Issue;
import com.streamsets.datacollector.validation.PipelineConfigurationValidator;
import com.streamsets.pipeline.api.OffsetCommitter;
import com.streamsets.pipeline.api.StageException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

import java.util.ArrayList;
import java.util.List;

public class ProductionPipelineBuilder {
  private static final Logger LOG = LoggerFactory.getLogger(ProductionPipelineBuilder.class);
  private static final String PRODUCTION_PIPELINE_SUFFIX = ":production";

  private final StageLibraryTask stageLib;
  private final String name;
  private final String rev;
  private final Configuration configuration;
  private final RuntimeInfo runtimeInfo;

  private final ProductionPipelineRunner runner;
  private final Observer observer;

  public ProductionPipelineBuilder(@Named("name") String name, @Named("rev") String rev,
                                   Configuration configuration, RuntimeInfo runtimeInfo,
                                   StageLibraryTask stageLib, ProductionPipelineRunner runner, Observer observer) {
    this.name = name;
    this.rev = rev;
    this.configuration = configuration;
    this.runtimeInfo = runtimeInfo;
    this.stageLib = stageLib;
    this.runner = runner;
    this.observer = observer;
  }

  public ProductionPipeline build(PipelineConfiguration pipelineConf)
    throws PipelineRuntimeException, StageException {
    PipelineConfigurationValidator validator = new PipelineConfigurationValidator(stageLib, name, pipelineConf);
    pipelineConf = validator.validate();
    if (validator.getIssues().hasIssues()) {
      throw new PipelineRuntimeException(ContainerError.CONTAINER_0158, ValidationUtil.getFirstIssueAsString(name,
        validator.getIssues()));
    }
    Pipeline pipeline = new Pipeline.Builder(stageLib, configuration, name + PRODUCTION_PIPELINE_SUFFIX, name, rev,
      pipelineConf).setObserver(observer).build(runner);

    if (pipeline.getSource() instanceof OffsetCommitter) {
      runner.setOffsetTracker(new ProductionSourceOffsetCommitterOffsetTracker(name, rev, runtimeInfo,
        (OffsetCommitter) pipeline.getSource()));
    } else {
      runner.setOffsetTracker(new ProductionSourceOffsetTracker(name, rev, runtimeInfo));
    }
    PipelineConfigBean pipelineConfigBean = PipelineBeanCreator.get().create(pipelineConf, new ArrayList<Issue>());
    return new ProductionPipeline(name, rev, pipelineConf, configuration, pipeline, pipelineConfigBean.shouldRetry);
  }

}
