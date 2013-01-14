tree grammar GroupTree;

options {
	tokenVocab=Group;
	output=AST;
    ASTLabelType=CommonTree;
}

@header {
package pt.ist.bennu.core.grouplanguage;
}

definition returns [Group value]
	:	g=group { $value = $g.value; } EOF!
	;

group returns [Group value]
	:	ANONYMOUS { $value = new Anonymous(); }
	|	ANYONE { $value = new Anyone(); }
	|	NOBODY { $value = new Nobody(); }
	|	LOGGED { $value = new Logged(); }
	|	people { $value = $people.value; }
	|	intersection { $value = $intersection.value; }
	|	union { $value = $union.value; }
	|	difference { $value = $difference.value; }
	|	negation { $value = $negation.value; }
	|	custom { $value = $custom.value; }
	;

intersection returns [Intersection value]
	@init {
		java.util.List<Group> children = new java.util.ArrayList<>();
	}
	:	^('&' (group { children.add($group.value); })+)
		{ $value = new Intersection(children); }
	;

union returns [Union value]
	@init {
		java.util.List<Group> children = new java.util.ArrayList<>();
	}
	:	^('|' (group { children.add($group.value); })+)
		{ $value = new Union(children); }
	;

difference returns [Difference value]
	@init {
		java.util.List<Group> children = new java.util.ArrayList<>();
	}
	:	^('-' (group { children.add($group.value); })+)
		{ $value = new Difference(children); }
	;

negation returns [Negation value]
	:	^('!' group)
		{ $value = new Negation($group.value); }
	;

custom returns [Custom value]
	@init {
		java.util.List<String> params = new java.util.ArrayList<>();
	}
	:	^(CUSTOM op=IDENTIFIER (arg=IDENTIFIER { params.add($arg.text); })*)
		{ $value = new Custom($op.text, params); }
	;

people returns [People value]
	@init {
		java.util.List<String> usernames = new java.util.ArrayList<>();
	}
	:	^(PEOPLE (username { usernames.add($username.value); })+)
		{ $value = new People(usernames); }
	;

username returns [String value]
	:	^(USERNAME IDENTIFIER)
		{ $value = $IDENTIFIER.text; }
	;
