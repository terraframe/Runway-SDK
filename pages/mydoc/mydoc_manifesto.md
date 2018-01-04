---
title: Our Software Engineering Manifesto
keywords: terraframe, gis, manifesto
last_updated: December 21, 2017
tags: [manifesto]
summary: "Our software engineering manifesto"
sidebar: mydoc_sidebar
permalink: mydoc_manifesto.html
folder: mydoc
---

## Check Your Ego at the Door

While all of us have egos, more often than not, they just get in the way and impede progress. If you are here with us, then by definition we think you are good and we expect you to demonstrate this through your contributions, but not with an attitude. We consider humility to be a virtue.

The reality is that you're probably not as good as you think your are, and we're not either; none of us are. All of us need to admit this (if only to ourselves) and get on with the job of getting better. We acknowledge and embrace the fact that as good as we actually (think we) are, we will always be lacking in some area. We view it as our personal responsibility to to better ourselves as engineers. We owe it to ourselves, our colleagues, and to society. After all, "software runs the world".

## Quality Is Paramount, but Elusive

Quality, defined as correctness, is paramount. But what is more important is our ability fix what's broken (and something will always broken, you just may not know it yet!). If we can't fix it, then all hope of achieving quality is forsaken. Can this be true? Unfortunately, it is.

Unlike the so-called "illities" of software [^fn1], such as understandability and maintainabilty, correctness is a dynamic property of software that emerges only as a side effect that is observable at runtime. Further, the best that we can do is access correctness relative to a description of some statically given set of inputs and their corresponding expected correct outputs. Given such a test set, we can state with certainty that the software is or is not correct, but only with respect to that set, and only after testing the software against each element (test case). If the software produces the expected output for each input, then we can say that the software is correct relative to that particular test set. Further, this statement only applies to the specific version of the software used during the test. If either the contents of the test set or the configuration of the software changes, then the statement will no longer apply.

## Software Engineering (SWE) Is Engineering

Software Engineering (SWE) is an engineering discipline, just as much as any of the traditional engineering disciplines are. What's different, however, is that SWE has far fewer constraints and a much, much larger number of degrees of freedom. And its always the degrees of freedom that lead to complexity in software. SWE itself is a dynamical system sensitive to initial conditions in which chaos can, and often does, emerge. This also often applies to the executing systems that are the result of our craft. And of course, the biggest source of degrees of freedom are humans.

## Governing Laws

The laws of Nature that govern SWE unfortunately are embodied in the psyches of human beings, not in the technology that we use. The fundamental problems are largely psychological and sociological, which are poorly understood and difficult to control. Yes, engineers working in traditional disciplines also suffer form this. However, they also have Natural Law to contend with (e.g., the laws of physics, chemistry), which works to their favor in that it constrains and limits the number of degrees they must cope with (lucky bastards). We software engineers are only constrained by the the inherent limitations of von Neuman computing, along with the limits of our own intellects. For most of us, that equates to an extremely large (practically infinite) number of degrees of freedom.

* All engineering is an optimization problem. Every decision we make is trade off against other possibilities. Further, is the context in which a decision is made that determines the relative value of one trade-off versus another. It is often the case that what makes one trade-off better than the alternatives has little or nothing to technical merits (i.e., in some cases, crap is the better choice). The key thing is that we understand and explicitly state the rationale for our decisions, their known limitations and an estimate of their future impacts and anticipated ameliorations.

## Foundations

* We find it unacceptable the belief that software development primarily consists of learning how to code. Attending a coding boot camp for several months is no substitute for a true foundation in computer science. Writing code consists of only a fraction of the knowledge of the art of being an exceptional software developer.
Practices
* We find it unacceptable that code is typically written that requires global system awareness in order to ensure correctness. This limits what our programs can ultimately do and leads to throwaway code.
* Process is only a guideline, it's not a goal. The history of software development is replete with examples in which process and process models are emphasized over technical realities. That said, there is much benefit from having standard operating procedures. It improves communications by defining work flow responsibilities, and expected/desired outcomes. However, process cannot overcome weakness in technical foundations, nor can it ensure outcomes. The key success factor with process lies in understanding the assumptions its underlying assumptions and their dependencies, along with recognizing where and when explicit adjustments must be made.

## Objective Truth

* We find it unacceptable that science is deliberately ignored and mocked. That truth is determined based on consent of the masses, rather than the scientific method. This is why fads come and go in our industry.
* In software development, we only have one tool for overcoming and handling complexity:
abstraction. Abstraction allows us to ignore irrelevant details and emphasize those that are principal.
However, it is unfortunate that the power of abstraction is often abused as a means for allow
arguments and evidence to be simplified or twisted to support a particular position. In so doing,
abstraction becomes a tool for obfuscation, making matters appear to be trivial or simpler than they
actually are. Process models are often susceptible to this practice in that they make certain assumptions that are not always true [^fn2]. Everything comes with an essential complexity and context that, if abstracted far enough, will loose the characteristics and constraints that are intrinsic to its essence.

## Miscellaneous
* Staying current with current research


## Footnotes

[^fn1]: The others are static properties whose intrinsic value is not dependent upon the dynamic behavior. That is, they are embodied directly in each static description of an artifact.

[^fn2]: Dan Turk, Robert France, and Bernhard Rumpe. Limitations of Agile Software Processes. In Proceedings Of The Third International Conference On Extreme Programming And Flexible Processes In Software Engineering (XP2000). Springer-Verlag, 2000. pp. 43-46.

{% include links.html %}
