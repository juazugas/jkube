/*
 * Copyright (c) 2019 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at:
 *
 *     https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.jkube.gradle.plugin.task;

import org.eclipse.jkube.gradle.plugin.KubernetesExtension;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

public class KubernetesHelmTask extends AbstractHelmTask {

  @Inject
  public KubernetesHelmTask(Class<? extends KubernetesExtension> extensionClass) {
    super(extensionClass);
    setDescription("Generates a Helm chart for the kubernetes resources.");
  }

  @Override
  public void run() {
    try {
      File manifest = kubernetesExtension.getKubernetesManifestOrDefault();
      if (manifest == null || !manifest.isFile()) {
        logManifestNotFoundWarning(manifest);
      }
      jKubeServiceHub.getHelmService().generateHelmCharts(kubernetesExtension.helm);
    } catch (IOException exception) {
      throw new IllegalStateException(exception.getMessage(), exception);
    }
  }

  protected void logManifestNotFoundWarning(File manifest) {
    kitLogger.warn("No kubernetes manifest file has been generated yet by the k8sResource task at: " + manifest);
  }
}
