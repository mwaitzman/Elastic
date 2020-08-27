	.text
	.file	"main.c"
	.section	.rodata.cst16,"aM",@progbits,16
	.p2align	4               # -- Begin function main
.LCPI0_0:
	.long	1067030938              # float 1.20000005
	.long	1067030938              # float 1.20000005
	.long	1067030938              # float 1.20000005
	.long	1067030938              # float 1.20000005
.LCPI0_1:
	.long	1057132380              # float 0.50999999
	.long	1057132380              # float 0.50999999
	.long	1057132380              # float 0.50999999
	.long	1057132380              # float 0.50999999
	.section	.rodata.cst4,"aM",@progbits,4
	.p2align	2
.LCPI0_2:
	.long	1067030938              # float 1.20000005
.LCPI0_3:
	.long	1057132380              # float 0.50999999
.LCPI0_4:
	.long	1082130432              # float 4
	.text
	.globl	main
	.p2align	4, 0x90
	.type	main,@function
main:                                   # @main
	.cfi_startproc
# %bb.0:
	pushq	%rax
	.cfi_def_cfa_offset 16
	movl	$419430400, %edi        # imm = 0x19000000
	callq	malloc
	movl	$2143289344, (%rax)     # imm = 0x7FC00000
	movl	$13, %ecx
	movaps	.LCPI0_0(%rip), %xmm0   # xmm0 = [1.20000005E+0,1.20000005E+0,1.20000005E+0,1.20000005E+0]
	movaps	.LCPI0_1(%rip), %xmm1   # xmm1 = [5.0999999E-1,5.0999999E-1,5.0999999E-1,5.0999999E-1]
	.p2align	4, 0x90
.LBB0_1:                                # =>This Inner Loop Header: Depth=1
	movups	-48(%rax,%rcx,4), %xmm2
	movups	-32(%rax,%rcx,4), %xmm3
	movaps	%xmm2, %xmm4
	addps	%xmm0, %xmm4
	movaps	%xmm3, %xmm5
	addps	%xmm0, %xmm5
	mulps	%xmm1, %xmm4
	mulps	%xmm1, %xmm5
	addps	%xmm2, %xmm4
	addps	%xmm3, %xmm5
	movups	%xmm4, -48(%rax,%rcx,4)
	movups	%xmm5, -32(%rax,%rcx,4)
	cmpq	$104857597, %rcx        # imm = 0x63FFFFD
	je	.LBB0_2
# %bb.3:                                #   in Loop: Header=BB0_1 Depth=1
	movups	-16(%rax,%rcx,4), %xmm2
	movups	(%rax,%rcx,4), %xmm3
	movaps	%xmm2, %xmm4
	addps	%xmm0, %xmm4
	movaps	%xmm3, %xmm5
	addps	%xmm0, %xmm5
	mulps	%xmm1, %xmm4
	mulps	%xmm1, %xmm5
	addps	%xmm2, %xmm4
	addps	%xmm3, %xmm5
	movups	%xmm4, -16(%rax,%rcx,4)
	movups	%xmm5, (%rax,%rcx,4)
	addq	$16, %rcx
	jmp	.LBB0_1
.LBB0_2:
	movss	419430372(%rax), %xmm2  # xmm2 = mem[0],zero,zero,zero
	movss	.LCPI0_2(%rip), %xmm0   # xmm0 = mem[0],zero,zero,zero
	movaps	%xmm2, %xmm3
	addss	%xmm0, %xmm3
	movss	.LCPI0_3(%rip), %xmm1   # xmm1 = mem[0],zero,zero,zero
	mulss	%xmm1, %xmm3
	addss	%xmm2, %xmm3
	movss	%xmm3, 419430372(%rax)
	movss	419430376(%rax), %xmm2  # xmm2 = mem[0],zero,zero,zero
	movaps	%xmm2, %xmm3
	addss	%xmm0, %xmm3
	mulss	%xmm1, %xmm3
	addss	%xmm2, %xmm3
	movss	%xmm3, 419430376(%rax)
	movss	419430380(%rax), %xmm2  # xmm2 = mem[0],zero,zero,zero
	movaps	%xmm2, %xmm3
	addss	%xmm0, %xmm3
	mulss	%xmm1, %xmm3
	addss	%xmm2, %xmm3
	movss	%xmm3, 419430380(%rax)
	movss	419430384(%rax), %xmm2  # xmm2 = mem[0],zero,zero,zero
	movaps	%xmm2, %xmm3
	addss	%xmm0, %xmm3
	mulss	%xmm1, %xmm3
	addss	%xmm2, %xmm3
	movss	%xmm3, 419430384(%rax)
	movss	419430388(%rax), %xmm2  # xmm2 = mem[0],zero,zero,zero
	movaps	%xmm2, %xmm3
	addss	%xmm0, %xmm3
	mulss	%xmm1, %xmm3
	addss	%xmm2, %xmm3
	movss	%xmm3, 419430388(%rax)
	movss	419430392(%rax), %xmm2  # xmm2 = mem[0],zero,zero,zero
	movaps	%xmm2, %xmm3
	addss	%xmm0, %xmm3
	mulss	%xmm1, %xmm3
	addss	%xmm2, %xmm3
	movss	%xmm3, 419430392(%rax)
	movss	419430396(%rax), %xmm2  # xmm2 = mem[0],zero,zero,zero
	addss	%xmm2, %xmm0
	mulss	%xmm1, %xmm0
	addss	%xmm2, %xmm0
	movss	%xmm0, 419430396(%rax)
	movss	4936(%rax), %xmm0       # xmm0 = mem[0],zero,zero,zero
	xorl	%eax, %eax
	ucomiss	.LCPI0_4(%rip), %xmm0
	seta	%al
	popq	%rcx
	.cfi_def_cfa_offset 8
	retq
.Lfunc_end0:
	.size	main, .Lfunc_end0-main
	.cfi_endproc
                                        # -- End function
	.ident	"clang version 10.0.0-4ubuntu1 "
	.section	".note.GNU-stack","",@progbits
	.addrsig
