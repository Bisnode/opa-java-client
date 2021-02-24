package age_rule


access_to_xmas_wine = {
    "allow": false,
    "reason": "You are not adult yet"
} {
    input.age < 18
} else = {
        "allow": true,
        "reason": "Xmas wine for you!"
}
