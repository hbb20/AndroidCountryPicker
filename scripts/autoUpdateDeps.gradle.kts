tasks.register("autoUpdateDeps") {
    group = "automation"
    description =
        "This task looks up available updates and update version in Dependencies.kt"
    // this taks uses output of dependencyUpdates to find new versions
    dependsOn(tasks.findByName("dependencyUpdates"))
    doLast {
        autoUpdateDependencies()
    }
}
