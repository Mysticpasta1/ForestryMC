/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.apiculture.multiblock;

import net.minecraft.item.ItemStack;

import forestry.api.apiculture.DefaultBeeListener;
import forestry.api.apiculture.IAlvearyComponent;
import forestry.api.apiculture.IBeeListener;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpeciesRoot;
import forestry.apiculture.blocks.BlockAlveary;
import forestry.apiculture.inventory.InventoryAlvearySieve;
import forestry.core.inventory.IInventoryAdapter;
import forestry.core.network.GuiId;
import forestry.core.tiles.ICrafter;

public class TileAlvearySieve extends TileAlvearyWithGui implements IAlvearyComponent.BeeListener {

	private final IBeeListener beeListener;
	private final InventoryAlvearySieve inventory;

	public TileAlvearySieve() {
		super(TileAlveary.SIEVE_META, GuiId.AlvearySieveGUI);
		this.inventory = new InventoryAlvearySieve(this);
		this.beeListener = new AlvearySieveBeeListener(inventory);
	}

	@Override
	public IInventoryAdapter getInternalInventory() {
		return inventory;
	}

	public ICrafter getCrafter() {
		return inventory;
	}

	@Override
	public IBeeListener getBeeListener() {
		return beeListener;
	}
	
	@Override
	public int getIcon(int side) {
		if (side == 0 || side == 1) {
			return BlockAlveary.BOTTOM;
		}
		return BlockAlveary.SIEVE;
	}

	static class AlvearySieveBeeListener extends DefaultBeeListener {
		private final InventoryAlvearySieve inventory;

		public AlvearySieveBeeListener(InventoryAlvearySieve inventory) {
			this.inventory = inventory;
		}

		@Override
		public boolean onPollenRetrieved(IIndividual pollen) {
			if (!inventory.canStorePollen()) {
				return false;
			}

			ISpeciesRoot speciesRoot = AlleleManager.alleleRegistry.getSpeciesRoot(pollen.getClass());

			ItemStack pollenStack = speciesRoot.getMemberStack(pollen, EnumGermlingType.POLLEN.ordinal());
			if (pollenStack != null) {
				inventory.storePollenStack(pollenStack);
				return true;
			}
			return false;
		}
	}
}
