package name_rule


access_to_chimney = {
    "allow": true,
    "reason": "Welcome Santa"
} {
    input.name == "SantaClaus"
} else = {
    "allow": false,
    "reason": "Can't jump into chimney"
}
