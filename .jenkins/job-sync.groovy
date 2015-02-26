/* variables */
String str = readFileFromWorkspace('.jenkins/build.conf')
println(str)
def cfg           = new ConfigSlurper().parse(str)

def git_branches  = [ 'master' ]


/* jobs */
def job_sync = "${cfg.jenkins_folder}/job-sync"

folder {
  name cfg.jenkins_folder
}

job {
  name job_sync
  description '.jenkins job sync'

  scm {
    git {
      remote {
        github "${cfg.github_owner}/${cfg.github_project}"
        
        credentials "${cfg.github_creds}"
      }
      
      git_branches.each { git_branch ->
        branch git_branch
      }
    }
  }

  triggers {
    scm '' // required for triggering
  }

  steps {
    dsl {
      removeAction 'DISABLE'
      external '.jenkins/*.groovy'
    }
  }
}
