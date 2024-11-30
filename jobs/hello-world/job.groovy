organizationFolder('jobs-v1') {
    job('hello-world') {
        steps {
            shell('echo "---- Hello World !!!!! poc Jcasc !!!!" ')
        }
    }
}
