package type_rule

allowed_party_members = {"SANTA", "ELF"}
presents_packaging = {
    "allow": true,
    "reason": sprintf("%v may pack presents for children", [input.name])
} {
    input.santaPartyMember == allowed_party_members[_]
}  else = {
    "allow" : false,
    "reason": "Only trusted members of Santa team may pack presents"
}
