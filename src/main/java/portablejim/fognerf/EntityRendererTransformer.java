package portablejim.fognerf;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 12/02/14
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class EntityRendererTransformer implements IClassTransformer {
    boolean obfuscated;
    HashMap<String, String> srgMap;

    public EntityRendererTransformer() {
        srgMap = new HashMap<String, String>();
        srgMap.put("setupFog", "func_78468_a");
        srgMap.put("getRespiration", "func_77501_a");
        srgMap.put("getWorldHasVoidParticles", "func_76564_j");
        srgMap.put("farPlaneDistance", "field_78530_s");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if("net.minecraft.client.renderer.EntityRenderer".equals(transformedName)) {
            obfuscated = !transformedName.equals(name);
            basicClass = transformSetupFog(name, basicClass);
        }
        return basicClass;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private byte[] transformSetupFog(String name, byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        try {
            for(MethodNode methodNode : classNode.methods) {
                String srgFunctionName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, methodNode.name, methodNode.desc);

                if(getCorrectName("setupFog").equals(srgFunctionName)) {
                    doTransformSetupFog(name, methodNode);
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            // Log error.
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private void doTransformSetupFog(String name, MethodNode methodNode) {
        int i = 0;
        while(!isMethodWithName(methodNode.instructions.get(i), "getRespiration")) {
            i++;
        }

        VarInsnNode variableCall = (VarInsnNode) methodNode.instructions.get(i-1);
        MethodInsnNode getRespirationCall = (MethodInsnNode) methodNode.instructions.get(i);
        i++;

        while(methodNode.instructions.get(i).getType() != AbstractInsnNode.METHOD_INSN) {
            i++;
        }
        i++;

        if(methodNode.instructions.get(i).getType() != AbstractInsnNode.JUMP_INSN) {
            return;
        }

        JumpInsnNode jump = (JumpInsnNode) methodNode.instructions.get(i);

        InsnList testIfZero = new InsnList();
        testIfZero.add(new VarInsnNode(variableCall.getOpcode(), variableCall.var));
        testIfZero.add(new MethodInsnNode(getRespirationCall.getOpcode(), getRespirationCall.owner, getRespirationCall.name, getRespirationCall.desc));
        testIfZero.add(new JumpInsnNode(Opcodes.IFNE, jump.label));
        testIfZero.add(new IntInsnNode(Opcodes.SIPUSH, GL11.GL_FOG_DENSITY));
        testIfZero.add(new LdcInsnNode(new Float("0.01")));
        testIfZero.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/lwjgl/opengl/GL11", "glFogf", "(IF)V"));
        methodNode.instructions.insertBefore(methodNode.instructions.get(i), testIfZero);

        i += 8;


        while(methodNode.instructions.get(i).getOpcode() != Opcodes.SIPUSH || ((IntInsnNode)methodNode.instructions.get(i)).operand != GL11.GL_FOG_DENSITY) {
            i++;
        }

        if(methodNode.instructions.get(i+1).getOpcode() != Opcodes.FCONST_2) {
            return;
        }

        methodNode.instructions.remove(methodNode.instructions.get(i+1));
        methodNode.instructions.insert(methodNode.instructions.get(i), new LdcInsnNode(new Float("0.3")));
        i++;

        while(methodNode.instructions.get(i).getOpcode() != Opcodes.GETFIELD || !isFieldWithName(methodNode.instructions.get(i), "farPlaneDistance")) {
            i++;
        }

        FieldInsnNode fieldCall = (FieldInsnNode) methodNode.instructions.get(i);
        VarInsnNode fieldStore = (VarInsnNode) methodNode.instructions.get(i+1);

        while(methodNode.instructions.get(i).getOpcode() != Opcodes.INVOKESTATIC || !((MethodInsnNode)methodNode.instructions.get(i)).name.equals("glFogi")) {
            i++;
        }

        InsnList setF1Value = new InsnList();
        setF1Value.add(new VarInsnNode(Opcodes.ALOAD, 0));
        setF1Value.add(new FieldInsnNode(fieldCall.getOpcode(), fieldCall.owner, fieldCall.name, fieldCall.desc));
        setF1Value.add(new VarInsnNode(fieldStore.getOpcode(), fieldStore.var));
        methodNode.instructions.insert(methodNode.instructions.get(i), setF1Value);
    }

    private String getCorrectName(String name) {
        return obfuscated ? srgMap.get(name) : name;
    }

    public boolean isMethodWithName(AbstractInsnNode instruction, String name) {
        if(instruction.getType() == AbstractInsnNode.METHOD_INSN) {
            MethodInsnNode methodNode = (MethodInsnNode)instruction;
            String srgName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(methodNode.owner, methodNode.name, methodNode.desc);
            return srgName.equals(getCorrectName(name));
        }
        return false;
    }

    public boolean isFieldWithName(AbstractInsnNode instruction, String name) {
        if(instruction.getType() == AbstractInsnNode.FIELD_INSN) {
            FieldInsnNode fieldNode = (FieldInsnNode) instruction;
            String srgName = FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(fieldNode.owner, fieldNode.name, fieldNode.desc);
            FMLLog.info("FOGNERF: %s | %s", srgName, name);
            return srgName.equals(getCorrectName(name));
        }
        return false;
    }
}
